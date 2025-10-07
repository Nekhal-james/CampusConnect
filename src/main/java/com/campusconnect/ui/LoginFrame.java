package com.campusconnect.ui;

import com.campusconnect.model.User;
import com.campusconnect.service.AuthService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class LoginFrame extends JFrame {
    private final JTextField emailField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);
    private final AuthService authService = new AuthService();

    public LoginFrame() {
        setTitle("CampusConnect - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        panel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Register");

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(loginBtn, gbc);
        gbc.gridx = 1;
        panel.add(registerBtn, gbc);

        add(panel);

        loginBtn.addActionListener(new LoginAction());
        registerBtn.addActionListener(e -> new RegisterDialog(this));
    }

    private class LoginAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            try {
                User user = authService.login(email, password);
                if (user != null) {
                    new DashboardFrame(user).setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(LoginFrame.this, "Invalid credentials.");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(LoginFrame.this, "Database error: " + ex.getMessage());
            }
        }
    }

    private static class RegisterDialog extends JDialog {
        private final JTextField nameField = new JTextField(20);
        private final JTextField emailField = new JTextField(20);
        private final JPasswordField passwordField = new JPasswordField(20);
        private final JComboBox<String> roleCombo = new JComboBox<>(new String[]{"Student", "Organizer", "Admin"});
        private final AuthService authService = new AuthService();

        public RegisterDialog(Frame parent) {
            super(parent, "Register", true);
            setSize(350, 250);
            setLocationRelativeTo(parent);

            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);

            gbc.gridx = 0; gbc.gridy = 0;
            panel.add(new JLabel("Name:"), gbc);
            gbc.gridx = 1;
            panel.add(nameField, gbc);

            gbc.gridx = 0; gbc.gridy = 1;
            panel.add(new JLabel("Email:"), gbc);
            gbc.gridx = 1;
            panel.add(emailField, gbc);

            gbc.gridx = 0; gbc.gridy = 2;
            panel.add(new JLabel("Password:"), gbc);
            gbc.gridx = 1;
            panel.add(passwordField, gbc);

            gbc.gridx = 0; gbc.gridy = 3;
            panel.add(new JLabel("Role:"), gbc);
            gbc.gridx = 1;
            panel.add(roleCombo, gbc);

            JButton registerBtn = new JButton("Register");
            gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
            panel.add(registerBtn, gbc);

            add(panel);

            registerBtn.addActionListener(e -> {
                String name = nameField.getText();
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                String role = (String) roleCombo.getSelectedItem();

                try {
                    if (authService.register(name, email, password, role)) {
                        JOptionPane.showMessageDialog(this, "Registration successful!");
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Registration failed.");
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
                }
            });

            setVisible(true);
        }
    }
}
