package com.campusconnect.ui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import com.campusconnect.model.Event;
import com.campusconnect.model.Feedback;
import com.campusconnect.model.User;
import com.campusconnect.service.FeedbackService;
import com.campusconnect.service.RegistrationService;

public class EventPanel extends JPanel {
    private final Event event;
    private final User user;
    private final RegistrationService registrationService;
    private final DashboardFrame parentFrame;

    public EventPanel(Event event, User user, RegistrationService registrationService, DashboardFrame parentFrame) {
        this.event = event;
        this.user = user;
        this.registrationService = registrationService;
        this.parentFrame = parentFrame;

        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5),
                BorderFactory.createTitledBorder(event.getTitle())
        ));

        JTextArea details = new JTextArea();
        details.setText("Date: " + event.getDate() + "\nTime: " + event.getTime() + "\nVenue: " + event.getVenue() + "\n\nDescription:\n" + event.getDescription());
        details.setEditable(false);
        details.setOpaque(false); // Make it blend with the panel background
        details.setLineWrap(true);
        details.setWrapStyleWord(true);
        add(new JScrollPane(details), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton registerBtn = new JButton("Register");
        JButton feedbackBtn = new JButton("Feedback");
        JButton unregisterBtn = new JButton("Unregister");

        registerBtn.addActionListener(new RegisterAction());
        unregisterBtn.addActionListener(new UnregisterAction());
        feedbackBtn.addActionListener(new FeedbackAction());

        buttonPanel.add(registerBtn);
        buttonPanel.add(unregisterBtn);
        buttonPanel.add(feedbackBtn);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private class RegisterAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                registrationService.register(event.getId(), user.getId(), timestamp);
                JOptionPane.showMessageDialog(EventPanel.this, "Successfully registered for " + event.getTitle() + "!", "Success", JOptionPane.INFORMATION_MESSAGE);
                parentFrame.refreshEvents();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(EventPanel.this, "Error during registration: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class UnregisterAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                registrationService.unregister(event.getId(), user.getId());
                JOptionPane.showMessageDialog(EventPanel.this, "Successfully unregistered from " + event.getTitle() + "!", "Success", JOptionPane.INFORMATION_MESSAGE);
                parentFrame.refreshEvents();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(EventPanel.this, "Error during unregistration: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class FeedbackAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Window parentWindow = SwingUtilities.getWindowAncestor(EventPanel.this);
            new FeedbackDialog(parentWindow, event.getId(), user.getId());
        }
    }

    private static class FeedbackDialog extends JDialog {
        private final JComboBox<Integer> ratingCombo = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
        private final JTextArea commentArea = new JTextArea(5, 20);
        private final int eventId;
        private final int userId;
        private final FeedbackService feedbackService = new FeedbackService();

        public FeedbackDialog(Window parent, int eventId, int userId) {
            super(parent, "Submit Feedback", ModalityType.APPLICATION_MODAL);
            this.eventId = eventId;
            this.userId = userId;

            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.anchor = GridBagConstraints.WEST;

            gbc.gridx = 0;
            gbc.gridy = 0;
            panel.add(new JLabel("Rating:"), gbc);

            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            panel.add(ratingCombo, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.fill = GridBagConstraints.NONE;
            panel.add(new JLabel("Comments:"), gbc);

            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.BOTH;
            gbc.weightx = 1.0;
            gbc.weighty = 1.0;
            commentArea.setLineWrap(true);
            commentArea.setWrapStyleWord(true);
            panel.add(new JScrollPane(commentArea), gbc);

            JButton submitBtn = new JButton("Submit");
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            gbc.fill = GridBagConstraints.NONE;
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.weightx = 0;
            gbc.weighty = 0;
            panel.add(submitBtn, gbc);

            add(panel);
            pack();
            setLocationRelativeTo(parent);

            submitBtn.addActionListener(e -> {
                Integer rating = (Integer) ratingCombo.getSelectedItem();
                String comments = commentArea.getText();

                if (comments == null || comments.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter a comment.", "Input Required", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // **THIS IS THE CORRECTED LINE**
                Feedback feedback = new Feedback(eventId, userId, rating, comments);
                
                try {
                    feedbackService.submitFeedback(feedback);
                    JOptionPane.showMessageDialog(this, "Feedback submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error submitting feedback: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            setVisible(true);
        }
    }
}