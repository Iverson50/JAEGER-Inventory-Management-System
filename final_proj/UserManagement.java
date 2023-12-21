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
import java.util.List;

public class UserManagement extends JPanel {
    private List<User> userList;
    private Connection connection;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/inventorymanagement";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public UserManagement(List<User> userList) {
        this.userList = userList;
        setLayout(new GridBagLayout());
        setBackground(new Color(91, 50, 86));
        GridBagConstraints c = new GridBagConstraints();

        JPanel credentialsPanel = new JPanel(new GridBagLayout());
        c.insets = new Insets(5, 5, 5, 5);

        c.gridx = 0;
        c.gridy = 0;
        JLabel username = new JLabel("Username:");
        username.setFont(new Font("High Tower Text", Font.BOLD, 30));
        username.setForeground(Color.WHITE);
        credentialsPanel.add(username, c);

        JTextField usernameField = new JTextField(20);
        c.gridx = 1;
        credentialsPanel.add(usernameField, c);
        credentialsPanel.setBackground(new Color(91, 50, 86));

        c.gridx = 0;
        c.gridy = 1;
        JLabel password = new JLabel("Password:");
        password.setFont(new Font("High Tower Text", Font.BOLD, 30));
        password.setForeground(Color.WHITE);
        credentialsPanel.add(password, c);

        JPasswordField passwordField = new JPasswordField(20);
        c.gridx = 1;
        credentialsPanel.add(passwordField, c);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBackground(new Color(91, 50, 86));

        Dimension buttonSize = new Dimension(100, 30);
        Dimension buttonSize2 = new Dimension(130, 30);

        JButton registerButton = new JButton("Register");
        registerButton.setBackground(new Color(91, 50, 86));
        registerButton.setPreferredSize(buttonSize);
        registerButton.setFont(new Font("Cambria", Font.BOLD, 17));

        JButton authenticateButton = new JButton("Authenticate");
        authenticateButton.setBackground(new Color(91, 50, 86));
        authenticateButton.setPreferredSize(buttonSize2);
        authenticateButton.setFont(new Font("Cambria", Font.BOLD, 16));

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                char[] password = passwordField.getPassword();

                User newUser = new User(username, new String(password));
                userList.add(newUser);
                registerUserToDatabase(username, new String(password));

                JOptionPane.showMessageDialog(UserManagement.this, "User registered successfully!");

                usernameField.setText("");
                passwordField.setText("");
            }
        });

        authenticateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                char[] password = passwordField.getPassword();

                User foundUser = findUser(username, new String(password));
                boolean authenticated = authenticateUserInDatabase(username, new String(password));

                if (foundUser != null && authenticated) {
                    JOptionPane.showMessageDialog(UserManagement.this, "Authentication successful!");
                } else {
                    JOptionPane.showMessageDialog(UserManagement.this, "Authentication failed. Invalid credentials.");
                }

                passwordField.setText("");
            }
        });

        buttonsPanel.add(registerButton);
        buttonsPanel.add(authenticateButton);

        c.gridx = 0;
        c.gridy = 0;
        add(credentialsPanel, c);
        c.gridy = 1;
        add(buttonsPanel, c);

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to connect to the database. Error: " + e.getMessage());
        }
    }

    public static class User {
        private String username;
        private String password;

        public User(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }

    private User findUser(String username, String password) {
        for (User user : userList) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public void registerUserToDatabase(String username, String password) {
        try {
            String sql = "INSERT INTO tbluseracc (username, password) VALUES (?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, username);
                statement.setString(2, password);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to register user to the database.");
        }
    }

    public boolean authenticateUserInDatabase(String username, String password) {
        try {
            String sql = "SELECT * FROM tbluseracc WHERE username = ? AND password = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, username);
                statement.setString(2, password);
                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
