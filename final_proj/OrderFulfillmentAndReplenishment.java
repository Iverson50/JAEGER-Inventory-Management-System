package final_proj;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderFulfillmentAndReplenishment extends JPanel {
    private DataManager dataManager;
    private DefaultListModel<String> itemListModel;
    private JList<String> itemJList;
    private JTextArea logTextArea;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/inventorymanagement";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public OrderFulfillmentAndReplenishment(DataManager dataManager) {
        this.dataManager = dataManager;

        setLayout(new BorderLayout());
        setBackground(new Color(74, 3, 54));

        JPanel orderFulfillmentPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        orderFulfillmentPanel.setBorder(BorderFactory.createTitledBorder("Order Fulfillment Section"));

        JButton pickItemsButton = new JButton("Pick Items");
        JButton markShippedButton = new JButton("Mark Items as Shipped");

        pickItemsButton.addActionListener(e -> handlePickItemsButtonAction());
        markShippedButton.addActionListener(e -> handleMarkShippedButtonAction());

        orderFulfillmentPanel.add(new JLabel());
        orderFulfillmentPanel.add(pickItemsButton);
        orderFulfillmentPanel.add(markShippedButton);

        JPanel stockReplenishmentPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        stockReplenishmentPanel.setBorder(BorderFactory.createTitledBorder("Stock Replenishment Section"));

        JButton receiveStockButton = new JButton("Receive New Stock");
        JButton triggerReorderButton = new JButton("Trigger Reorder Request");

        receiveStockButton.addActionListener(e -> handleReceiveStockButtonAction());
        triggerReorderButton.addActionListener(e -> handleTriggerReorderButtonAction("Triggering reorder request"));

        stockReplenishmentPanel.add(new JLabel());
        stockReplenishmentPanel.add(receiveStockButton);
        stockReplenishmentPanel.add(triggerReorderButton);

        logTextArea = new JTextArea(10, 30);
        logTextArea.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(logTextArea);

        itemListModel = new DefaultListModel<>();
        itemJList = new JList<>(itemListModel);
        itemJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane listScrollPane = new JScrollPane(itemJList);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(orderFulfillmentPanel, BorderLayout.NORTH);
        leftPanel.add(stockReplenishmentPanel, BorderLayout.SOUTH);

        add(leftPanel, BorderLayout.WEST);
        add(listScrollPane, BorderLayout.CENTER);
        add(logScrollPane, BorderLayout.SOUTH);

        updateItemListFromDatabase();
    }

    private void handlePickItemsButtonAction() {
        String selectedItem = itemJList.getSelectedValue();
        if (selectedItem != null) {
            try {
                updateOrderFulfilledTable(selectedItem);

                String message = "Item has been picked for order: " + selectedItem;
                JOptionPane.showMessageDialog(this, message, "Order Picked", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error picking items for order: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an item.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateOrderFulfilledTable(String selectedItem) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            int productID = getProductID(selectedItem);

            String sql = "INSERT INTO tblorderfulfillment (product_ID, orderItem) VALUES (?, ?)";

            try (PreparedStatement statement = connection.prepareStatement(sql,
                    PreparedStatement.RETURN_GENERATED_KEYS)) {
                statement.setInt(1, productID);
                statement.setString(2, selectedItem);

                statement.executeUpdate();

                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int orderID = generatedKeys.getInt(1);
                        System.out.println("Inserted into tblorderfulfillment with order_ID: " + orderID);
                    }
                }
            }
        }
    }

    private int getProductID(String productName) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT product_ID FROM tblproduct WHERE name = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, productName);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("product_ID");
                    }
                }
            }
        }
        throw new SQLException("Product not found");
    }

    private void handleMarkShippedButtonAction() {
        String selectedItem = itemJList.getSelectedValue();
        if (selectedItem != null) {
            try {
                markItemAsShipped(selectedItem);

                String message = "Item has been marked as shipped: " + selectedItem;
                JOptionPane.showMessageDialog(this, message, "Item Shipped", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error marking item as shipped: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an item.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void markItemAsShipped(String selectedItem) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            int productID = getProductID(selectedItem);

            String sql = "INSERT INTO tblshipped (product_ID, isShipped) VALUES (?, ?)";

            try (PreparedStatement statement = connection.prepareStatement(sql,
                    PreparedStatement.RETURN_GENERATED_KEYS)) {
                statement.setInt(1, productID);
                statement.setBoolean(2, true);

                statement.executeUpdate();

                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int shipID = generatedKeys.getInt(1);
                        System.out.println("Inserted into tblshipped with ship_ID: " + shipID);
                    }
                }
            }
        }
    }

    private void handleReceiveStockButtonAction() {
        String selectedItem = itemJList.getSelectedValue();
        if (selectedItem != null) {
            try {
                receiveNewStock(selectedItem);

                String message = "New stock received for item: " + selectedItem;
                JOptionPane.showMessageDialog(this, message, "Stock Received", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error receiving new stock: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an item.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void receiveNewStock(String selectedItem) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            int productID = getProductID(selectedItem);

            String sql = "INSERT INTO tblstock (product_ID, isReceived) VALUES (?, ?)";

            try (PreparedStatement statement = connection.prepareStatement(sql,
                    PreparedStatement.RETURN_GENERATED_KEYS)) {
                statement.setInt(1, productID);
                statement.setBoolean(2, true);

                statement.executeUpdate();

                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int stockID = generatedKeys.getInt(1);
                        System.out.println("Inserted into tblstock with stock_ID: " + stockID);
                    }
                }
            }
        }
    }

    private void handleTriggerReorderButtonAction(String action) {
        String selectedItem = itemJList.getSelectedValue();
        if (selectedItem != null) {
            try {
                switch (action) {
                    case "Marking items as shipped":
                        markItemAsShipped(selectedItem);
                        break;
                    case "Triggering reorder request":
                        triggerReorderRequest(selectedItem);
                        break;
                    default:
                        JOptionPane.showMessageDialog(this, action + " for item: " + selectedItem);
                        break;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an item.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void triggerReorderRequest(String selectedItem) {
        try {
            int productID = getProductID(selectedItem);

            insertReorderRequest(productID, selectedItem);

            String message = "Reorder request triggered for item: " + selectedItem;
            JOptionPane.showMessageDialog(this, message, "Reorder Request Triggered", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error triggering reorder request: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void insertReorderRequest(int productID, String selectedItem) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "INSERT INTO tblreorder (product_ID, reorderItem) VALUES (?, ?)";

            try (PreparedStatement statement = connection.prepareStatement(sql,
                    PreparedStatement.RETURN_GENERATED_KEYS)) {
                statement.setInt(1, productID);
                statement.setString(2, selectedItem);

                statement.executeUpdate();

                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int reorderID = generatedKeys.getInt(1);
                        System.out.println("Inserted into tblreorder with reorder_ID: " + reorderID);
                    }
                }
            }
        }
    }

    private void addNewItemToList(String itemName) {
        itemListModel.addElement(itemName);
    }

    private void updateItemListFromDatabase() {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT name FROM tblproduct";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                ResultSet resultSet = statement.executeQuery();

                itemListModel.clear();

                while (resultSet.next()) {
                    String itemName = resultSet.getString("name");
                    addNewItemToList(itemName);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error executing SQL query: " + ex.getMessage());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to connect to the database. Error: " + e.getMessage());
        }
    }
}
