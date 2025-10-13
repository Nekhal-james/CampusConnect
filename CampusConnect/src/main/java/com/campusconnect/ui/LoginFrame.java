package com.campusconnect.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.campusconnect.model.User;
import com.campusconnect.service.AuthService;
import com.campusconnect.util.ThemeUtil;

public class LoginFrame extends JFrame {
    private final JTextField emailField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);
    private final AuthService authService = new AuthService();

    public LoginFrame() {
        setTitle("CampusConnect - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        ThemeUtil.setAppTheme();
        createTopNavBar();

        // Use a content wrapper with GridBagLayout to robustly center the card panel
        JPanel contentWrapper = new JPanel(new GridBagLayout());
        contentWrapper.setBackground(ThemeUtil.BACKGROUND);
        add(contentWrapper, BorderLayout.CENTER);

        // Card Panel for the form elements
        JPanel cardPanel = ThemeUtil.createStyledPanel();
        cardPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Title
        JLabel titleLabel = new JLabel("Welcome to CampusConnect");
        titleLabel.setFont(ThemeUtil.TITLE_FONT);
        titleLabel.setForeground(ThemeUtil.DARK);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        cardPanel.add(titleLabel, gbc);

        // Subtitle
        JLabel subtitleLabel = new JLabel("Sign in to your account");
        subtitleLabel.setFont(ThemeUtil.BODY_FONT);
        subtitleLabel.setForeground(ThemeUtil.MEDIUM_ACCENT);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.insets = new Insets(0, 10, 20, 10);
        gbc.gridy = 1;
        cardPanel.add(subtitleLabel, gbc);

        // Email Label and Field
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.insets = new Insets(10, 10, 10, 5);
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(ThemeUtil.DARK);
        cardPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(10, 0, 10, 10);
        ThemeUtil.styleTextField(emailField);
        cardPanel.add(emailField, gbc);

        // Password Label and Field
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.insets = new Insets(10, 10, 10, 5);
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(ThemeUtil.DARK);
        cardPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(10, 0, 10, 10);
        ThemeUtil.styleTextField(passwordField);
        cardPanel.add(passwordField, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Sign Up");
        ThemeUtil.styleLoginButton(loginBtn);
        ThemeUtil.styleSignupButton(registerBtn);
        buttonPanel.add(loginBtn);
        buttonPanel.add(registerBtn);
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.insets = new Insets(10, 10, 10, 10);
        cardPanel.add(buttonPanel, gbc);

        // Add the fully constructed cardPanel to the centering wrapper
        contentWrapper.add(cardPanel);

        loginBtn.addActionListener(new LoginAction());
        registerBtn.addActionListener(e -> new RegisterDialog(this));
    }

    private void createTopNavBar() {
        JPanel navBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        navBar.setBackground(ThemeUtil.LIGHT_ACCENT);
        navBar.setPreferredSize(new Dimension(getWidth(), 55));
        JButton homeBtn = new JButton("Home");
        JButton loginBtn = new JButton("Login");
        JButton signupBtn = new JButton("Sign Up");
        ThemeUtil.styleNavButton(homeBtn);
        ThemeUtil.styleNavButton(loginBtn);
        ThemeUtil.styleNavButton(signupBtn);
        signupBtn.addActionListener(e -> new RegisterDialog(this));
        navBar.add(homeBtn);
        navBar.add(Box.createHorizontalStrut(25));
        navBar.add(loginBtn);
        navBar.add(Box.createHorizontalStrut(25));
        navBar.add(signupBtn);
        add(navBar, BorderLayout.NORTH);
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
                    JOptionPane.showMessageDialog(LoginFrame.this,
                        "Invalid credentials. Please try again.", "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(LoginFrame.this,
                    "Database error: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
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
            setSize(540, 500);
            setLocationRelativeTo(parent);
            setResizable(false);
            
            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.setBackground(ThemeUtil.BACKGROUND);
            mainPanel.setBorder(BorderFactory.createEmptyBorder(35, 35, 35, 35));
            add(mainPanel);

            JPanel cardPanel = ThemeUtil.createStyledPanel();
            cardPanel.setLayout(new GridBagLayout());
            mainPanel.add(cardPanel, BorderLayout.CENTER);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(10, 10, 10, 10);

            JLabel titleLabel = new JLabel("Create Account");
            titleLabel.setFont(ThemeUtil.HEADING_FONT);
            titleLabel.setForeground(ThemeUtil.DARK);
            titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
            gbc.gridwidth = 2;
            gbc.gridx = 0;
            gbc.gridy = 0;
            cardPanel.add(titleLabel, gbc);

            gbc.gridwidth = 1;
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.anchor = GridBagConstraints.LINE_END;
            gbc.insets = new Insets(10, 10, 10, 5);
            JLabel nameLabel = new JLabel("Name:");
            nameLabel.setForeground(ThemeUtil.DARK);
            cardPanel.add(nameLabel, gbc);

            gbc.gridx = 1;
            gbc.anchor = GridBagConstraints.LINE_START;
            gbc.insets = new Insets(10, 0, 10, 10);
            ThemeUtil.styleTextField(nameField);
            cardPanel.add(nameField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.anchor = GridBagConstraints.LINE_END;
            gbc.insets = new Insets(10, 10, 10, 5);
            JLabel emailLabel = new JLabel("Email:");
            emailLabel.setForeground(ThemeUtil.DARK);
            cardPanel.add(emailLabel, gbc);

            gbc.gridx = 1;
            gbc.anchor = GridBagConstraints.LINE_START;
            gbc.insets = new Insets(10, 0, 10, 10);
            ThemeUtil.styleTextField(emailField);
            cardPanel.add(emailField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.anchor = GridBagConstraints.LINE_END;
            gbc.insets = new Insets(10, 10, 10, 5);
            JLabel passwordLabel = new JLabel("Password:");
            passwordLabel.setForeground(ThemeUtil.DARK);
            cardPanel.add(passwordLabel, gbc);

            gbc.gridx = 1;
            gbc.anchor = GridBagConstraints.LINE_START;
            gbc.insets = new Insets(10, 0, 10, 10);
            ThemeUtil.styleTextField(passwordField);
            cardPanel.add(passwordField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 4;
            gbc.anchor = GridBagConstraints.LINE_END;
            gbc.insets = new Insets(10, 10, 10, 5);
            JLabel roleLabel = new JLabel("Role:");
            roleLabel.setForeground(ThemeUtil.DARK);
            cardPanel.add(roleLabel, gbc);

            gbc.gridx = 1;
            gbc.anchor = GridBagConstraints.LINE_START;
            gbc.insets = new Insets(10, 0, 10, 10);
            roleCombo.setFont(ThemeUtil.BODY_FONT);
            cardPanel.add(roleCombo, gbc);
            
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 0));
            buttonPanel.setBackground(Color.WHITE);
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
            
            JButton registerBtn = new JButton("Sign Up");
            JButton cancelBtn = new JButton("Cancel");
            ThemeUtil.styleSignupButton(registerBtn);
            ThemeUtil.styleDestructiveButton(cancelBtn);
            
            buttonPanel.add(registerBtn);
            buttonPanel.add(cancelBtn);
            
            gbc.gridwidth = 2;
            gbc.gridx = 0;
            gbc.gridy = 5;
            gbc.insets = new Insets(10, 10, 10, 10);
            cardPanel.add(buttonPanel, gbc);

            registerBtn.addActionListener(e -> {
                String name = nameField.getText();
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                String role = (String) roleCombo.getSelectedItem();

                try {
                    if (authService.register(name, email, password, role)) {
                        JOptionPane.showMessageDialog(this,
                            "Registration successful! You can now login.", "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(this,
                            "Registration failed. Email may already exist.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this,
                        "Database error: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            });

            cancelBtn.addActionListener(e -> dispose());
            setVisible(true);
        }
    }
}