/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author IVERSON
 */
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {

    public static void main(String[] args) {
        UserService userService = new UserService();
        JFrame frame = new JFrame("IMS Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel, userService);

        frame.setVisible(true);
    }

    private static void placeComponents(JPanel panel, UserService userService) {
        panel.setLayout(null);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(10, 20, 80, 25);
        panel.add(userLabel);

        JTextField userText = new JTextField(20);
        userText.setBounds(100, 20, 165, 25);
        panel.add(userText);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(10, 50, 80, 25);
        panel.add(passwordLabel);

        JPasswordField passwordText = new JPasswordField(20);
        passwordText.setBounds(100, 50, 165, 25);
        panel.add(passwordText);

        JButton loginButton = new JButton("login");
        loginButton.setBounds(10, 80, 80, 25);
        panel.add(loginButton);

        JButton registerButton = new JButton("register");
        registerButton.setBounds(100, 80, 80, 25);
        panel.add(registerButton);

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == loginButton) {
                    if (userService.authenticateUser(userText.getText(), String.valueOf(passwordText.getPassword()))) {
                        JOptionPane.showMessageDialog(null, "Login Successful");
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid username or password");
                    }
                } else if (e.getSource() == registerButton) {
                    if (userService.registerUser(userText.getText(), String.valueOf(passwordText.getPassword()))) {
                        JOptionPane.showMessageDialog(null, "Registration Successful");
                    } else {
                        JOptionPane.showMessageDialog(null, "Username already exists");
                    }
                }
            }
        };

        loginButton.addActionListener(actionListener);
        registerButton.addActionListener(actionListener);
    }
}