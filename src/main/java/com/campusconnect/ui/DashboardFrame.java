package com.campusconnect.ui;

import com.campusconnect.model.Event;
import com.campusconnect.model.User;
import com.campusconnect.service.EventService;
import com.campusconnect.service.RegistrationService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class DashboardFrame extends JFrame {
    private final User currentUser;
    private final EventService eventService = new EventService();
    private final RegistrationService registrationService = new RegistrationService();
    private final JPanel eventPanel = new JPanel();
    private final JScrollPane scrollPane = new JScrollPane(eventPanel);

    public DashboardFrame(User user) {
        this.currentUser = user;
        setTitle("Dashboard - " + user.getName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Welcome, " + user.getName() + " (" + user.getRole() + ")"));

        if ("Organizer".equals(user.getRole()) || "Admin".equals(user.getRole())) {
            JButton createBtn = new JButton("Create Event");
            createBtn.addActionListener(e -> new CreateEventDialog(this, currentUser.getId()));
            topPanel.add(createBtn);
        }

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        refreshEvents();
    }

    public void refreshEvents() {
        eventPanel.removeAll();
        eventPanel.setLayout(new BoxLayout(eventPanel, BoxLayout.Y_AXIS));

        try {
            List<Event> events = eventService.getAllEvents();
            for (Event event : events) {
                eventPanel.add(new EventPanel(event, currentUser, registrationService, this));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading events: " + e.getMessage());
        }

        eventPanel.revalidate();
        eventPanel.repaint();
    }

    private static class CreateEventDialog extends JDialog {
        private final JTextField titleField = new JTextField(20);
        private final JTextField dateField = new JTextField(20);
        private final JTextField timeField = new JTextField(20);
        private final JTextField venueField = new JTextField(20);
        private final JTextArea descArea = new JTextArea(5, 20);
        private final int organizerId;
        private final EventService eventService = new EventService();
        private final DashboardFrame parent;

        public CreateEventDialog(Frame parent, int organizerId) {
            super(parent, "Create Event", true);
            this.organizerId = organizerId;
            this.parent = (DashboardFrame) parent;
            setSize(400, 400);
            setLocationRelativeTo(parent);

            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);

            gbc.gridx = 0; gbc.gridy = 0;
            panel.add(new JLabel("Title:"), gbc);
            gbc.gridx = 1;
            panel.add(titleField, gbc);

            gbc.gridx = 0; gbc.gridy = 1;
            panel.add(new JLabel("Date (YYYY-MM-DD):"), gbc);
            gbc.gridx = 1;
            panel.add(dateField, gbc);

            gbc.gridx = 0; gbc.gridy = 2;
            panel.add(new JLabel("Time (HH:MM):"), gbc);
            gbc.gridx = 1;
            panel.add(timeField, gbc);

            gbc.gridx = 0; gbc.gridy = 3;
            panel.add(new JLabel("Venue:"), gbc);
            gbc.gridx = 1;
            panel.add(venueField, gbc);

            gbc.gridx = 0; gbc.gridy = 4;
            panel.add(new JLabel("Description:"), gbc);
            gbc.gridx = 1;
            panel.add(new JScrollPane(descArea), gbc);

            JButton createBtn = new JButton("Create");
            gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
            panel.add(createBtn, gbc);

            add(panel);

            createBtn.addActionListener(new CreateAction());

            setVisible(true);
        }

        private class CreateAction implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = titleField.getText();
                String date = dateField.getText();
                String time = timeField.getText();
                String venue = venueField.getText();
                String desc = descArea.getText();

                Event event = new Event(title, date, time, venue, organizerId, desc);
                try {
                    eventService.createEvent(event);
                    JOptionPane.showMessageDialog(CreateEventDialog.this, "Event created!");
                    parent.refreshEvents();
                    dispose();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(CreateEventDialog.this, "Error: " + ex.getMessage());
                }
            }
        }
    }
}
