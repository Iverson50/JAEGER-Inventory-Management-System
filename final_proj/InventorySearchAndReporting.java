package final_proj;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InventorySearchAndReporting extends JPanel {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/inventorymanagement";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public InventorySearchAndReporting() {
        setLayout(new GridBagLayout());
        setBackground(new Color(66, 28, 82));
        GridBagConstraints c = new GridBagConstraints();

        JPanel searchPanel = new JPanel(new GridBagLayout());
        c.insets = new Insets(5, 5, 5, 5);

        c.gridx = 0;
        c.gridy = 0;
        JLabel SB = new JLabel("Search By:");
        SB.setFont(new Font("Cambria", Font.BOLD, 20));
        SB.setForeground(Color.WHITE);
        SB.setBackground(new Color(66, 28, 82));
        searchPanel.add(SB, c);
        searchPanel.setBackground(new Color(66, 28, 82));

        JComboBox<String> searchOptions = new JComboBox<>(new String[] { "Name", "Category", "Location" });
        searchOptions.setFont(new Font("Cambria", Font.PLAIN, 13));
        c.gridx = 1;
        searchPanel.add(searchOptions, c);

        c.gridx = 0;
        c.gridy = 1;
        JLabel ST = new JLabel("Search Term:");
        ST.setFont(new Font("Cambria", Font.BOLD, 20));
        ST.setForeground(Color.WHITE);
        ST.setBackground(new Color(66, 28, 82));
        searchPanel.add(ST, c);

        JTextField searchTermField = new JTextField(15);
        c.gridx = 1;
        searchPanel.add(searchTermField, c);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBackground(new Color(66, 28, 82));

        JButton searchButton = new JButton("Search");
        searchButton.setBackground(new Color(66, 28, 82));
        searchButton.setFont(new Font("Cambria", Font.PLAIN, 15));

        JButton generateReportButton = new JButton("Generate Report");
        generateReportButton.setBackground(new Color(66, 28, 82));
        generateReportButton.setFont(new Font("Cambria", Font.PLAIN, 15));

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchOption = (String) searchOptions.getSelectedItem();
                String searchTerm = searchTermField.getText();
                performSearch(searchOption, searchTerm);
            }
        });

        generateReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchTerm = searchTermField.getText();
                generateInventoryReport(searchTerm);
            }
        });

        buttonsPanel.add(searchButton);
        buttonsPanel.add(generateReportButton);

        JPanel customReportPanel = new JPanel(new GridBagLayout());
        c.gridx = 0;
        c.gridy = 2;
        customReportPanel.setBackground(new Color(66, 28, 82));
        add(customReportPanel, c);

        JTextArea customReportTextArea = new JTextArea(10, 30);
        JScrollPane customReportScrollPane = new JScrollPane(customReportTextArea);

        JButton createCustomReportButton = new JButton("Create Custom Report");
        createCustomReportButton.setFont(new Font("Cambria", Font.BOLD, 15));
        createCustomReportButton.setBackground(new Color(66, 28, 82));

        createCustomReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String customReport = customReportTextArea.getText();
                String searchTerm = searchTermField.getText();
                insertCustomReport(customReport, searchTerm);
            }
        });

        c.gridx = 0;
        c.gridy = 0;
        JLabel cusRep = new JLabel("Custom Report:");
        cusRep.setFont(new Font("Cambria", Font.BOLD, 20));
        cusRep.setForeground(Color.WHITE);
        cusRep.setBackground(new Color(66, 28, 82));
        customReportPanel.add(cusRep, c);

        c.gridy = 1;
        customReportPanel.add(customReportScrollPane, c);
        c.gridy = 2;
        customReportPanel.add(createCustomReportButton, c);

        c.gridx = 0;
        c.gridy = 0;
        add(searchPanel, c);
        c.gridy = 1;
        add(buttonsPanel, c);
    }

    private void performSearch(String searchOption, String searchTerm) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM tblproduct WHERE " + searchOption + " LIKE ?";

            System.out.println("SQL Query: " + sql);

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, "%" + searchTerm + "%");

                ResultSet resultSet = statement.executeQuery();

                StringBuilder resultMessage = new StringBuilder("Search Results:\n");
                while (resultSet.next()) {
                    resultMessage.append("Product Name: ").append(resultSet.getString("name")).append("\n");

                    if ("Name".equalsIgnoreCase(searchOption)) {
                        resultMessage.append("Location: ").append(resultSet.getString("location")).append("\n");
                    } else if ("Category".equalsIgnoreCase(searchOption)) {
                        resultMessage.append("Name: ").append(resultSet.getString("name")).append("\n");
                        resultMessage.append("Location: ").append(resultSet.getString("location")).append("\n");
                    } else if ("Location".equalsIgnoreCase(searchOption)) {
                        resultMessage.append("Name: ").append(resultSet.getString("name")).append("\n");
                        resultMessage.append("Category: ").append(resultSet.getString("category")).append("\n");
                    }
                }

                JOptionPane.showMessageDialog(this, resultMessage.toString());

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error executing search query: " + ex.getMessage());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to connect to the database. Error: " + e.getMessage());
        }
    }

    private void generateInventoryReport(String searchTerm) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM tblproduct WHERE name LIKE ? OR category LIKE ? OR location LIKE ?";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                for (int i = 1; i <= 3; i++) {
                    statement.setString(i, "%" + searchTerm + "%");
                }

                ResultSet resultSet = statement.executeQuery();

                StringBuilder reportMessage = new StringBuilder("Inventory Report:\n");

                while (resultSet.next()) {
                    reportMessage.append("Product Name: ").append(resultSet.getString("name")).append("\n");
                    reportMessage.append("Category: ").append(resultSet.getString("category")).append("\n");
                    reportMessage.append("Location: ").append(resultSet.getString("location")).append("\n");
                    reportMessage.append("----------\n");
                }

                if (reportMessage.length() > "Inventory Report:\n".length()) {
                    JOptionPane.showMessageDialog(this, reportMessage.toString());
                } else {
                    JOptionPane.showMessageDialog(this, "No data found for the report.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error executing query: " + ex.getMessage());
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to connect to the database. Error: " + ex.getMessage());
        }
    }

    private void insertCustomReport(String customReport, String searchTerm) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "INSERT INTO tblcustomreport (customreport, product) VALUES (?, ?)";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, customReport);
                statement.setString(2, searchTerm);

                int rowsAffected = statement.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Custom report inserted successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to insert custom report.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error executing insert query: " + ex.getMessage());
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to connect to the database. Error: " + ex.getMessage());
        }
    }
}
