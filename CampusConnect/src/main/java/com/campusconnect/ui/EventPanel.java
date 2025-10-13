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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import com.campusconnect.model.Event;
import com.campusconnect.model.Feedback;
import com.campusconnect.model.User;
import com.campusconnect.service.AuthService;
import com.campusconnect.service.EventService;
import com.campusconnect.service.FeedbackService;
import com.campusconnect.service.RegistrationService;
import com.campusconnect.util.ThemeUtil;

public class EventPanel extends JPanel {
    private final Event event;
    private final User user;
    private final RegistrationService registrationService;
    private final EventService eventService = new EventService();
    private final DashboardFrame parent;

    public EventPanel(Event event, User user, RegistrationService registrationService, DashboardFrame parent) {
        this.event = event;
        this.user = user;
        this.registrationService = registrationService;
        this.parent = parent;

        setLayout(new BorderLayout(0, 20));
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ThemeUtil.LIGHT_ACCENT, 1, true),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        setBackground(Color.WHITE);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(ThemeUtil.LIGHT_ACCENT);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        JLabel titleLabel = new JLabel(event.getTitle());
        titleLabel.setFont(ThemeUtil.HEADING_FONT);
        titleLabel.setForeground(ThemeUtil.DARK);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        // Details
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBackground(Color.WHITE);
        add(detailsPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.gridx = 0;
        gbc.insets = new Insets(8, 0, 8, 20);
        gbc.weightx = 0;
        gbc.gridy = 0;
        detailsPanel.add(new JLabel("Date:"), gbc);
        gbc.gridy = 1;
        detailsPanel.add(new JLabel("Time:"), gbc);
        gbc.gridy = 2;
        detailsPanel.add(new JLabel("Venue:"), gbc);
        gbc.gridy = 3;
        detailsPanel.add(new JLabel("Description:"), gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.weightx = 1.0;
        gbc.gridy = 0;
        detailsPanel.add(new JLabel(event.getDate()), gbc);
        gbc.gridy = 1;
        detailsPanel.add(new JLabel(event.getTime()), gbc);
        gbc.gridy = 2;
        detailsPanel.add(new JLabel(event.getVenue()), gbc);
        gbc.gridy = 3;
        JTextArea descArea = new JTextArea(event.getDescription());
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setOpaque(false);
        descArea.setEditable(false);
        descArea.setFont(ThemeUtil.BODY_FONT);
        detailsPanel.add(descArea, gbc);

        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        if ("Organizer".equals(user.getRole()) && user.getId() == event.getOrganizerId()) {
            JButton editBtn = new JButton("Edit Event");
            JButton deleteBtn = new JButton("Delete Event");
            JButton viewFeedbackBtn = new JButton("View Feedback");

            // Apply the same destructive button styling to Edit button for consistency
            ThemeUtil.styleSecondaryButton(editBtn);  // Changed to secondary style
            ThemeUtil.styleDestructiveButton(deleteBtn);
            ThemeUtil.styleAffirmativeButton(viewFeedbackBtn);

            editBtn.addActionListener(e -> {
                Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
                new DashboardFrame.EditEventDialog(parentFrame, event, parent);
            });
            deleteBtn.addActionListener(e -> {
                int result = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to permanently delete this event?", "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (result == JOptionPane.YES_OPTION) {
                    try {
                        eventService.deleteEvent(event.getId());
                        JOptionPane.showMessageDialog(this, "Event deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        parent.refreshEvents();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(this, "Error deleting event: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            viewFeedbackBtn.addActionListener(e -> {
                Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
                new ViewFeedbackDialog(parentFrame, event.getId());
            });

            buttonPanel.add(editBtn);
            buttonPanel.add(deleteBtn);
            buttonPanel.add(viewFeedbackBtn);
        } else {
            JButton registerBtn = new JButton("Register");
            JButton unregisterBtn = new JButton("Unregister");
            JButton feedbackBtn = new JButton("Give Feedback");

            ThemeUtil.styleAffirmativeButton(registerBtn);
            ThemeUtil.styleDestructiveButton(unregisterBtn);
            ThemeUtil.styleAffirmativeButton(feedbackBtn);

            registerBtn.addActionListener(new RegisterAction());
            unregisterBtn.addActionListener(new UnregisterAction());
            feedbackBtn.addActionListener(new FeedbackAction());

            buttonPanel.add(registerBtn);
            buttonPanel.add(unregisterBtn);
            buttonPanel.add(feedbackBtn);
        }
        return buttonPanel;
    }

    private class RegisterAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                registrationService.register(event.getId(), user.getId(), timestamp);
                JOptionPane.showMessageDialog(EventPanel.this,
                    "Successfully registered for " + event.getTitle() + "!",
                    "Registration Success", JOptionPane.INFORMATION_MESSAGE);
                parent.refreshEvents();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(EventPanel.this,
                    "Error registering: " + ex.getMessage(), "Registration Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class UnregisterAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int result = JOptionPane.showConfirmDialog(EventPanel.this,
                "Are you sure you want to unregister from " + event.getTitle() + "?",
                "Confirm Unregistration", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (result == JOptionPane.YES_OPTION) {
                try {
                    registrationService.unregister(event.getId(), user.getId());
                    JOptionPane.showMessageDialog(EventPanel.this,
                        "Successfully unregistered from " + event.getTitle() + "!",
                        "Unregistration Success", JOptionPane.INFORMATION_MESSAGE);
                    parent.refreshEvents();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(EventPanel.this,
                        "Error unregistering: " + ex.getMessage(), "Unregistration Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private class FeedbackAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(EventPanel.this);
            new FeedbackDialog(parentFrame, event.getId(), user.getId());
        }
    }

    private static class ViewFeedbackDialog extends JDialog {
        private final FeedbackService feedbackService = new FeedbackService();
        private final AuthService authService = new AuthService();

        public ViewFeedbackDialog(Frame parent, int eventId) {
            super(parent, "Event Feedback", true);
            setSize(600, 400);
            setLocationRelativeTo(parent);

            String[] columnNames = {"User", "Rating", "Comments"};
            DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
                 @Override
                 public boolean isCellEditable(int row, int column) {
                    return false; // Make table cells not editable
                 }
            };
            JTable table = new JTable(model);
            ThemeUtil.styleTable(table);
            table.setRowHeight(28);

            try {
                List<Feedback> feedbackList = feedbackService.getFeedbackForEvent(eventId);
                if (feedbackList.isEmpty()) {
                    model.addRow(new Object[]{"-", "-", "No feedback submitted yet."});
                } else {
                    for (Feedback feedback : feedbackList) {
                        User feedbackUser = authService.findUserById(feedback.getUserId());
                        String userName = (feedbackUser != null) ? feedbackUser.getName() : "Unknown User";
                        model.addRow(new Object[]{userName, feedback.getRating() + " / 5", feedback.getComments()});
                    }
                }
            } catch (SQLException e) {
                model.addRow(new Object[]{"-", "-", "Error loading feedback."});
            }

            JScrollPane scrollPane = new JScrollPane(table);
            add(scrollPane, BorderLayout.CENTER);
            setVisible(true);
        }
    }

    private static class FeedbackDialog extends JDialog {
        private final JComboBox<Integer> ratingCombo = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
        private final JTextArea commentArea = new JTextArea(7, 25);
        private final int eventId;
        private final int userId;
        private final FeedbackService feedbackService = new FeedbackService();

        public FeedbackDialog(Frame parent, int eventId, int userId) {
            super(parent, "Submit Feedback", true);
            this.eventId = eventId;
            this.userId = userId;
            setSize(580, 480);
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
            gbc.insets = new Insets(15, 15, 15, 15);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            
            JLabel titleLabel = new JLabel("Event Feedback");
            titleLabel.setFont(ThemeUtil.HEADING_FONT);
            titleLabel.setForeground(ThemeUtil.DARK);
            titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
            gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
            cardPanel.add(titleLabel, gbc);
            
            gbc.gridwidth = 1;
            gbc.gridx = 0; gbc.gridy = 1;
            JLabel ratingLabel = new JLabel("Rating:");
            ratingLabel.setForeground(ThemeUtil.DARK);
            ratingLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            cardPanel.add(ratingLabel, gbc);
            
            gbc.gridx = 1;
            ratingCombo.setFont(ThemeUtil.BODY_FONT);
            cardPanel.add(ratingCombo, gbc);
            
            gbc.gridx = 0; gbc.gridy = 2;
            JLabel commentLabel = new JLabel("Comments:");
            commentLabel.setForeground(ThemeUtil.DARK);
            commentLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            cardPanel.add(commentLabel, gbc);
            
            gbc.gridx = 1;
            ThemeUtil.styleTextArea(commentArea);
            commentArea.setLineWrap(true);
            commentArea.setWrapStyleWord(true);
            JScrollPane commentScrollPane = new JScrollPane(commentArea);
            commentScrollPane.setPreferredSize(new Dimension(300, 120));
            cardPanel.add(commentScrollPane, gbc);
            
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 0));
            buttonPanel.setBackground(Color.WHITE);
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
            
            JButton submitBtn = new JButton("Submit Feedback");
            JButton cancelBtn = new JButton("Cancel");
            ThemeUtil.styleAffirmativeButton(submitBtn);
            ThemeUtil.styleDestructiveButton(cancelBtn);
            
            buttonPanel.add(submitBtn);
            buttonPanel.add(cancelBtn);
            
            gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
            cardPanel.add(buttonPanel, gbc);
            
            submitBtn.addActionListener(e -> {
                int rating = (Integer) ratingCombo.getSelectedItem();
                String comments = commentArea.getText().trim();
                
                if (comments.isEmpty()) {
                    JOptionPane.showMessageDialog(this, 
                        "Please provide some comments for your feedback.", 
                        "Validation Error", 
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Feedback feedback = new Feedback(eventId, userId, rating, comments);
                try {
                    feedbackService.submitFeedback(feedback);
                    JOptionPane.showMessageDialog(this, 
                        "Thank you for your feedback!", 
                        "Feedback Submitted", 
                        JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, 
                        "Error submitting feedback: " + ex.getMessage(), 
                        "Submission Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            });
            
            cancelBtn.addActionListener(e -> dispose());
            setVisible(true);
        }
    }
}
