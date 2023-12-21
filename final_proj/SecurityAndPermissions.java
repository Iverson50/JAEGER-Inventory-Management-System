package final_proj;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SecurityAndPermissions extends JPanel {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/inventorymanagement";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    private List<EnhancedUser> userList;
    private DefaultListModel<String> listModel;
    private JList<String> userListJList;

    public SecurityAndPermissions(List<EnhancedUser> userList) {
        this.userList = userList;
        listModel = new DefaultListModel<>();
        setLayout(new GridBagLayout());
        setBackground(new Color(67, 41, 77));
        GridBagConstraints c = new GridBagConstraints();

        JPanel rolesPanel = new JPanel(new GridBagLayout());
        rolesPanel.setBorder(BorderFactory.createTitledBorder("User List"));
        rolesPanel.setBackground(new Color(230, 230, 230));
        c.insets = new Insets(10, 10, 10, 10);

        c.gridx = 0;
        c.gridy = 0;
        JLabel select = new JLabel("Select a User:");
        select.setFont(new Font("Calibri", Font.BOLD, 17));
        rolesPanel.add(select, c);

        userListJList = new JList<>(listModel);
        userListJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane listScrollPane = new JScrollPane(userListJList);
        listScrollPane.setPreferredSize(new Dimension(200, 150));

        c.gridx = 0;
        c.gridy = 1;
        rolesPanel.add(listScrollPane, c);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBackground(new Color(67, 41, 77));

        Dimension buttonSize = new Dimension(150, 30);
        JButton viewPermissionsButton = new JButton("View Permissions");

        viewPermissionsButton.setPreferredSize(buttonSize);
        viewPermissionsButton.setBackground(new Color(67, 41, 77));
        viewPermissionsButton.setFont(new Font("Cambria", Font.BOLD, 14));
        viewPermissionsButton.addActionListener(e -> {
            int selectedIndex = userListJList.getSelectedIndex();
            if (selectedIndex != -1) {
                EnhancedUser selectedUser = userList.get(selectedIndex);
                viewPermissions(selectedUser);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a user to view permissions.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
            updateUsersList();
        });

        buttonsPanel.add(viewPermissionsButton);

        c.gridx = 0;
        c.gridy = 0;
        add(rolesPanel, c);
        c.gridy = 1;
        add(buttonsPanel, c);

        updateUsersList();
    }

    private void updateUsersList() {
        listModel.clear();
        for (EnhancedUser user : userList) {
            listModel.addElement(user.getUsername());
        }
    }

    private void viewPermissions(EnhancedUser user) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM tbluseracc WHERE username = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, user.getUsername());
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        System.out.println("Username: " + resultSet.getString("username"));
                        System.out.println("Roles: " + resultSet.getString("roles"));
                        System.out.println("Permissions: " + resultSet.getString("permissions"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error accessing the database: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static class EnhancedUser extends UserManagement.User {
        private List<String> roles;
        private List<String> permissions;

        public EnhancedUser(String username, List<String> roles, List<String> permissions) {
            super(username, null);
            this.roles = roles;
            this.permissions = permissions;
        }

        public List<String> getRoles() {
            return roles;
        }

        public List<String> getPermissions() {
            return permissions;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Security and Permissions Demo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(new SecurityAndPermissions(List.of(
                    new EnhancedUser("User1", List.of("Admin"), List.of("Read", "Write")),
                    new EnhancedUser("User2", List.of("Editor"), List.of("Read")))));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
