package final_proj;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductManagement extends JPanel {
    private List<Product> productList;
    private JTextField nameField;
    private JTextField descriptionField;
    private JTextField categoryField;
    private JTextField locationField;
    private JTextField quantityField;
    private DefaultListModel<Product> listModel;
    private JList<Product> productListJList;
    private Connection connection;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/inventorymanagement";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public static class Product {
        private String name;
        private String description;
        private String category;
        private String location;
        private int quantity;

        public Product(String name, String description, String category, String location, int quantity) {
            this.name = name;
            this.description = description;
            this.category = category;
            this.location = location;
            this.quantity = quantity;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public ProductManagement(List<Product> productList) {
        this.productList = productList;
        listModel = new DefaultListModel<>();
        setLayout(new GridBagLayout());
        setBackground(new Color(48, 25, 52));
        GridBagConstraints c = new GridBagConstraints();

        JPanel productInfoPanel = new JPanel(new GridBagLayout());
        productInfoPanel.setBackground(new Color(48, 25, 52));
        c.insets = new Insets(5, 5, 5, 5);

        c.gridx = 0;
        c.gridy = 0;

        JLabel name = new JLabel("Name:");
        name.setFont(new Font("Cambria", Font.BOLD, 20));
        name.setForeground(Color.WHITE);
        productInfoPanel.add(name, c);

        nameField = new JTextField(15);
        c.gridx = 1;
        productInfoPanel.add(nameField, c);

        c.gridx = 0;
        c.gridy = 1;

        JLabel description = new JLabel("Description:");
        description.setFont(new Font("Cambria", Font.BOLD, 20));
        description.setForeground(Color.WHITE);
        productInfoPanel.add(description, c);

        descriptionField = new JTextField(15);
        c.gridx = 1;
        productInfoPanel.add(descriptionField, c);

        c.gridx = 0;
        c.gridy = 2;

        JLabel category = new JLabel("Category:");
        category.setFont(new Font("Cambria", Font.BOLD, 20));
        category.setForeground(Color.WHITE);
        productInfoPanel.add(category, c);

        categoryField = new JTextField(15);
        c.gridx = 1;
        productInfoPanel.add(categoryField, c);

        c.gridx = 0;
        c.gridy = 3;
        JLabel location = new JLabel("Location:");
        location.setFont(new Font("Cambria", Font.BOLD, 20));
        location.setForeground(Color.WHITE);
        productInfoPanel.add(location, c);

        locationField = new JTextField(15);
        c.gridx = 1;
        productInfoPanel.add(locationField, c);

        c.gridx = 0;
        c.gridy = 4;
        JLabel quantity = new JLabel("Quantity:");
        quantity.setFont(new Font("Cambria", Font.BOLD, 20));
        quantity.setForeground(Color.WHITE);
        productInfoPanel.add(quantity, c);

        quantityField = new JTextField(7);
        c.gridx = 1;
        productInfoPanel.add(quantityField, c);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBackground(new Color(48, 25, 52));

        Dimension buttonSize = new Dimension(120, 30);

        JButton addProductButton = new JButton("Add Product");
        addProductButton.setBackground(new Color(48, 25, 52));
        addProductButton.setPreferredSize(buttonSize);
        addProductButton.setFont(new Font("Cambria", Font.BOLD, 12));

        JButton updateProductButton = new JButton("Update Product");
        updateProductButton.setBackground(new Color(48, 25, 52));
        updateProductButton.setPreferredSize(buttonSize);
        updateProductButton.setFont(new Font("Cambria", Font.BOLD, 12));

        JButton deleteProductButton = new JButton("Delete Product");
        deleteProductButton.setBackground(new Color(48, 25, 52));
        deleteProductButton.setPreferredSize(buttonSize);
        deleteProductButton.setFont(new Font("Cambria", Font.BOLD, 12));

        addProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProduct();
            }
        });

        updateProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateProduct();
            }
        });

        deleteProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteProduct();
            }
        });

        buttonsPanel.add(addProductButton);
        buttonsPanel.add(updateProductButton);
        buttonsPanel.add(deleteProductButton);

        productListJList = new JList<>(listModel);
        productListJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane listScrollPane = new JScrollPane(productListJList);
        c.gridx = 2;
        c.gridy = 0;
        add(listScrollPane, c);

        c.gridx = 0;
        c.gridy = 0;
        add(productInfoPanel, c);
        c.gridy = 1;
        add(buttonsPanel, c);

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to connect to the database. Error: " + e.getMessage());
        }

        loadProductsFromDatabase();
    }

    private void addProduct() {
        String name = nameField.getText();
        String description = descriptionField.getText();
        String category = categoryField.getText();
        String location = locationField.getText();
        String quantityText = quantityField.getText();

        if (name.isEmpty() || description.isEmpty() || category.isEmpty() || location.isEmpty()
                || quantityText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityText);
            Product newProduct = new Product(name, description, category, location, quantity);
            productList.add(newProduct);

            registerProductToDatabase(name, description, category, location, quantity);

            nameField.setText("");
            descriptionField.setText("");
            categoryField.setText("");
            locationField.setText("");
            quantityField.setText("");

            updateProductList();
            JOptionPane.showMessageDialog(this, "Product added successfully.", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid quantity.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateProduct() {
        int selectedIndex = productListJList.getSelectedIndex();

        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product to update.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Product selectedProduct = productList.get(selectedIndex);
        String newName = JOptionPane.showInputDialog(this, "Enter new name:", selectedProduct.getName());
        String newDescription = JOptionPane.showInputDialog(this, "Enter new description:",
                selectedProduct.getDescription());
        String newCategory = JOptionPane.showInputDialog(this, "Enter new category:", selectedProduct.getCategory());
        String newLocation = JOptionPane.showInputDialog(this, "Enter new location:", selectedProduct.getLocation());
        String newQuantity = JOptionPane.showInputDialog(this, "Enter new quantity:", selectedProduct.getQuantity());

        if (newName != null && newDescription != null && newCategory != null && newLocation != null
                && newQuantity != null) {
            try {
                int quantity = Integer.parseInt(newQuantity);

                selectedProduct.setName(newName);
                selectedProduct.setDescription(newDescription);
                selectedProduct.setCategory(newCategory);
                selectedProduct.setLocation(newLocation);
                selectedProduct.setQuantity(quantity);

                updateProductInDatabase(selectedProduct.getName(), newName, newDescription, newCategory, newLocation,
                        quantity);

                updateProductList();
                JOptionPane.showMessageDialog(this, "Product updated successfully.", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid quantity.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteProduct() {
        int selectedIndex = productListJList.getSelectedIndex();

        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product to delete.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this product?",
                "Confirmation", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            Product deletedProduct = productList.remove(selectedIndex);

            deleteProductFromDatabase(deletedProduct.getName());

            updateProductList();
            JOptionPane.showMessageDialog(this, "Product deleted successfully.", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void updateProductList() {
        listModel.clear();
        for (Product product : productList) {
            listModel.addElement(product);
        }
    }

    private void registerProductToDatabase(String name, String description, String category, String location,
            int quantity) {
        try {

            String sql = "INSERT INTO tblproduct (name, description, category, location, quantity) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, name);
                statement.setString(2, description);
                statement.setString(3, category);
                statement.setString(4, location);
                statement.setInt(5, quantity);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to register product to the database. Error: " + e.getMessage());
        }
    }

    private void deleteProductFromDatabase(String productName) {
        try {

            String sql = "DELETE FROM tblproduct WHERE name = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, productName);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to delete product from the database. Error: " + e.getMessage());
        }
    }

    private void updateProductInDatabase(String oldName, String newName, String newDescription, String newCategory,
            String newLocation, int newQuantity) {
        try {
            String sql = "UPDATE tblproduct SET name = ?, description = ?, category = ?, location = ?, quantity = ? WHERE name = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, newName);
                statement.setString(2, newDescription);
                statement.setString(3, newCategory);
                statement.setString(4, newLocation);
                statement.setInt(5, newQuantity);
                statement.setString(6, oldName);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to update product in the database. Error: " + e.getMessage());
        }
    }

    private void loadProductsFromDatabase() {
        try {

            String sql = "SELECT * FROM tblproduct";
            try (Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(sql)) {

                while (resultSet.next()) {
                    String name = resultSet.getString("name");
                    String description = resultSet.getString("description");
                    String category = resultSet.getString("category");
                    String location = resultSet.getString("location");
                    int quantity = resultSet.getInt("quantity");

                    Product product = new Product(name, description, category, location, quantity);
                    productList.add(product);
                }

                updateProductList();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load products from the database. Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Product Management");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            List<Product> productList = new ArrayList<>();
            frame.getContentPane().add(new ProductManagement(productList));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
