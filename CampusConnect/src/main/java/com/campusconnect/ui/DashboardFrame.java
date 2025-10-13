package com.campusconnect.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.campusconnect.model.Event;
import com.campusconnect.model.Registration;
import com.campusconnect.model.User;
import com.campusconnect.service.EventService;
import com.campusconnect.service.RegistrationService;
import com.campusconnect.util.ThemeUtil;

public class DashboardFrame extends JFrame {
    private final User currentUser;
    private final EventService eventService = new EventService();
    private final RegistrationService registrationService = new RegistrationService();
    private final JPanel contentPanel = new JPanel();
    private final JScrollPane scrollPane = new JScrollPane(contentPanel);
    private JPanel currentViewPanel;

    public DashboardFrame(User user) {
        this.currentUser = user;
        setTitle("CampusConnect - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 750);
        setLocationRelativeTo(null);

        ThemeUtil.setAppTheme();
        createTopNavBar();

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(ThemeUtil.BACKGROUND);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        add(mainPanel, BorderLayout.CENTER);

        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(ThemeUtil.BACKGROUND);

        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        showHomeView();
    }

    private void createTopNavBar() {
        JPanel navBar = new JPanel(new BorderLayout());
        navBar.setBackground(ThemeUtil.LIGHT_ACCENT);
        navBar.setPreferredSize(new Dimension(getWidth(), 60));
        navBar.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 25));

        JLabel appNameLabel = new JLabel("CampusConnect");
        appNameLabel.setFont(ThemeUtil.TITLE_FONT);
        appNameLabel.setForeground(ThemeUtil.DARK);
        navBar.add(appNameLabel, BorderLayout.WEST);

        JPanel navButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        navButtonsPanel.setBackground(ThemeUtil.LIGHT_ACCENT);
        JButton homeBtn = new JButton("Home");
        ThemeUtil.styleNavButton(homeBtn);
        homeBtn.addActionListener(e -> showHomeView());
        navButtonsPanel.add(homeBtn);

        if ("Student".equals(currentUser.getRole())) {
            JButton registeredEventsBtn = new JButton("My Events");
            ThemeUtil.styleNavButton(registeredEventsBtn);
            registeredEventsBtn.addActionListener(e -> showRegisteredEventsView());
            navButtonsPanel.add(registeredEventsBtn);
        } else if ("Organizer".equals(currentUser.getRole()) || "Admin".equals(currentUser.getRole())) {
            JButton yourEventsBtn = new JButton("Your Events");
            ThemeUtil.styleNavButton(yourEventsBtn);
            yourEventsBtn.addActionListener(e -> showYourEventsView());
            navButtonsPanel.add(yourEventsBtn);

            JButton participantsBtn = new JButton("Participants");
            ThemeUtil.styleNavButton(participantsBtn);
            participantsBtn.addActionListener(e -> showParticipantsView());
            navButtonsPanel.add(participantsBtn);
        }
        navBar.add(navButtonsPanel, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setOpaque(false);
        JPanel rightWrapper = new JPanel(new GridBagLayout());
        rightWrapper.setOpaque(false);

        JLabel userLabel = new JLabel(currentUser.getName() + " (" + currentUser.getRole() + ")");
        userLabel.setFont(ThemeUtil.BODY_FONT);
        userLabel.setForeground(ThemeUtil.DARK);
        rightPanel.add(userLabel);

        JButton logoutBtn = new JButton("Logout");
        ThemeUtil.styleDestructiveButton(logoutBtn);
        logoutBtn.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?",
                    "Confirm Logout", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (result == JOptionPane.YES_OPTION) {
                new LoginFrame().setVisible(true);
                dispose();
            }
        });
        rightPanel.add(logoutBtn);

        rightWrapper.add(rightPanel);
        navBar.add(rightWrapper, BorderLayout.EAST);
        add(navBar, BorderLayout.NORTH);
    }

    private void showHomeView() {
        updateContentView("Home");
        JPanel eventsPanel = new JPanel();
        eventsPanel.setLayout(new BoxLayout(eventsPanel, BoxLayout.Y_AXIS));
        eventsPanel.setBackground(ThemeUtil.BACKGROUND);
        eventsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        try {
            List<Event> events = eventService.getAllEvents();
            if (events.isEmpty()) {
                JLabel emptyLabel = new JLabel("No events available. Check back later!");
                emptyLabel.setFont(ThemeUtil.BODY_FONT);
                emptyLabel.setForeground(ThemeUtil.MEDIUM_ACCENT);
                emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
                emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                eventsPanel.add(Box.createVerticalGlue());
                eventsPanel.add(emptyLabel);
                eventsPanel.add(Box.createVerticalGlue());
            } else {
                for (Event event : events) {
                    eventsPanel.add(new EventPanel(event, currentUser, registrationService, this));
                    eventsPanel.add(Box.createVerticalStrut(20));
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading events: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        currentViewPanel.add(eventsPanel, BorderLayout.CENTER);
        refreshContentView();
    }

    private void showRegisteredEventsView() {
        updateContentView("My Registered Events");
        JPanel eventsPanel = new JPanel();
        eventsPanel.setLayout(new BoxLayout(eventsPanel, BoxLayout.Y_AXIS));
        eventsPanel.setBackground(ThemeUtil.BACKGROUND);
        eventsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        try {
            List<Registration> registrations = registrationService.getUserRegistrations(currentUser.getId());
            if (registrations.isEmpty()) {
                JLabel emptyLabel = new JLabel("You haven't registered for any events yet.");
                emptyLabel.setFont(ThemeUtil.BODY_FONT);
                emptyLabel.setForeground(ThemeUtil.MEDIUM_ACCENT);
                emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
                emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                eventsPanel.add(Box.createVerticalGlue());
                eventsPanel.add(emptyLabel);
                eventsPanel.add(Box.createVerticalGlue());
            } else {
                List<Event> allEvents = eventService.getAllEvents();
                boolean hasEvents = false;
                for (Event event : allEvents) {
                    for (Registration reg : registrations) {
                        if (reg.getEventId() == event.getId()) {
                            eventsPanel.add(new EventPanel(event, currentUser, registrationService, this));
                            eventsPanel.add(Box.createVerticalStrut(20));
                            hasEvents = true;
                            break;
                        }
                    }
                }
                if (!hasEvents) {
                     JLabel emptyLabel = new JLabel("Could not find details for your registered events.");
                     emptyLabel.setFont(ThemeUtil.BODY_FONT);
                     emptyLabel.setForeground(ThemeUtil.MEDIUM_ACCENT);
                     emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
                     emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                     eventsPanel.add(Box.createVerticalGlue());
                     eventsPanel.add(emptyLabel);
                     eventsPanel.add(Box.createVerticalGlue());
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading registered events: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        currentViewPanel.add(eventsPanel, BorderLayout.CENTER);
        refreshContentView();
    }

    private void showYourEventsView() {
        updateContentView("Your Created Events");
        JPanel eventsPanel = new JPanel();
        eventsPanel.setLayout(new BoxLayout(eventsPanel, BoxLayout.Y_AXIS));
        eventsPanel.setBackground(ThemeUtil.BACKGROUND);
        eventsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        try {
            List<Event> allEvents = eventService.getAllEvents();
            boolean hasYourEvents = false;
            for (Event event : allEvents) {
                if (event.getOrganizerId() == currentUser.getId()) {
                    eventsPanel.add(new EventPanel(event, currentUser, registrationService, this));
                    eventsPanel.add(Box.createVerticalStrut(20));
                    hasYourEvents = true;
                }
            }
            if (!hasYourEvents) {
                JLabel emptyLabel = new JLabel("You haven't created any events yet.");
                emptyLabel.setFont(ThemeUtil.BODY_FONT);
                emptyLabel.setForeground(ThemeUtil.MEDIUM_ACCENT);
                emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
                emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                JButton createBtn = new JButton("Create Your First Event");
                ThemeUtil.styleAffirmativeButton(createBtn);
                createBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
                createBtn.addActionListener(e -> {
                    Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(DashboardFrame.this);
                    new CreateEventDialog(parentFrame, currentUser.getId());
                });

                eventsPanel.add(Box.createVerticalGlue());
                eventsPanel.add(emptyLabel);
                eventsPanel.add(Box.createVerticalStrut(25));
                eventsPanel.add(createBtn);
                eventsPanel.add(Box.createVerticalGlue());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading your events: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        currentViewPanel.add(eventsPanel, BorderLayout.CENTER);
        refreshContentView();
    }

    private void showParticipantsView() {
        updateContentView("Event Participants");
        JLabel comingSoonLabel = new JLabel("Participant management is coming soon!");
        comingSoonLabel.setFont(ThemeUtil.HEADING_FONT);
        comingSoonLabel.setForeground(ThemeUtil.MEDIUM_ACCENT);
        comingSoonLabel.setHorizontalAlignment(SwingConstants.CENTER);
        currentViewPanel.add(comingSoonLabel, BorderLayout.CENTER);
        refreshContentView();
    }

    private void updateContentView(String title) {
        contentPanel.removeAll();
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(ThemeUtil.BACKGROUND);
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(ThemeUtil.TITLE_FONT);
        titleLabel.setForeground(ThemeUtil.DARK);
        titlePanel.add(titleLabel);
        contentPanel.add(titlePanel, BorderLayout.NORTH);
        currentViewPanel = new JPanel(new BorderLayout());
        currentViewPanel.setBackground(ThemeUtil.BACKGROUND);
        contentPanel.add(currentViewPanel, BorderLayout.CENTER);
    }

    private void refreshContentView() {
        contentPanel.revalidate();
        contentPanel.repaint();
        SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(0));
    }

    public void refreshEvents() {
        showHomeView();
    }

    public static class EditEventDialog extends JDialog {
        private final JTextField titleField;
        private final JTextField dateField;
        private final JTextField timeField;
        private final JTextField venueField;
        private final JTextArea descArea;
        private final Event eventToEdit;
        private final EventService eventService = new EventService();
        private final DashboardFrame parentFrame;

        public EditEventDialog(Frame parent, Event event, DashboardFrame parentFrame) {
            super(parent, "Edit Event", true);
            this.eventToEdit = event;
            this.parentFrame = parentFrame;
            setSize(580, 580);
            setLocationRelativeTo(parent);
            setResizable(false);

            titleField = new JTextField(event.getTitle(), 25);
            dateField = new JTextField(event.getDate(), 25);
            timeField = new JTextField(event.getTime(), 25);
            venueField = new JTextField(event.getVenue(), 25);
            descArea = new JTextArea(event.getDescription(), 6, 25);

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

            JLabel titleLabel = new JLabel("Edit Event Details");
            titleLabel.setFont(ThemeUtil.HEADING_FONT);
            titleLabel.setForeground(ThemeUtil.DARK);
            titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
            gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
            cardPanel.add(titleLabel, gbc);

            gbc.gridwidth = 1;
            gbc.gridx = 0; gbc.gridy = 1;
            JLabel titleLabel2 = new JLabel("Title:");
            titleLabel2.setForeground(ThemeUtil.DARK);
            titleLabel2.setHorizontalAlignment(SwingConstants.RIGHT);
            cardPanel.add(titleLabel2, gbc);

            gbc.gridx = 1;
            ThemeUtil.styleTextField(titleField);
            cardPanel.add(titleField, gbc);
            
            gbc.gridx = 0; gbc.gridy = 2;
            JLabel dateLabel = new JLabel("Date (YYYY-MM-DD):");
            dateLabel.setForeground(ThemeUtil.DARK);
            dateLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            cardPanel.add(dateLabel, gbc);
            
            gbc.gridx = 1;
            ThemeUtil.styleTextField(dateField);
            cardPanel.add(dateField, gbc);
            
            gbc.gridx = 0; gbc.gridy = 3;
            JLabel timeLabel = new JLabel("Time (HH:MM):");
            timeLabel.setForeground(ThemeUtil.DARK);
            timeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            cardPanel.add(timeLabel, gbc);
            
            gbc.gridx = 1;
            ThemeUtil.styleTextField(timeField);
            cardPanel.add(timeField, gbc);
            
            gbc.gridx = 0; gbc.gridy = 4;
            JLabel venueLabel = new JLabel("Venue:");
            venueLabel.setForeground(ThemeUtil.DARK);
            venueLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            cardPanel.add(venueLabel, gbc);
            
            gbc.gridx = 1;
            ThemeUtil.styleTextField(venueField);
            cardPanel.add(venueField, gbc);
            
            gbc.gridx = 0; gbc.gridy = 5;
            JLabel descLabel = new JLabel("Description:");
            descLabel.setForeground(ThemeUtil.DARK);
            descLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            cardPanel.add(descLabel, gbc);
            
            gbc.gridx = 1;
            ThemeUtil.styleTextArea(descArea);
            descArea.setLineWrap(true);
            descArea.setWrapStyleWord(true);
            JScrollPane descScrollPane = new JScrollPane(descArea);
            descScrollPane.setPreferredSize(new Dimension(300, 120));
            cardPanel.add(descScrollPane, gbc);
            
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 0));
            buttonPanel.setBackground(Color.WHITE);
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
            
            JButton saveBtn = new JButton("Save Changes");
            JButton cancelBtn = new JButton("Cancel");
            ThemeUtil.styleAffirmativeButton(saveBtn);
            ThemeUtil.styleDestructiveButton(cancelBtn);
            
            buttonPanel.add(saveBtn);
            buttonPanel.add(cancelBtn);
            
            gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
            cardPanel.add(buttonPanel, gbc);
            
            saveBtn.addActionListener(e -> saveChanges());
            cancelBtn.addActionListener(e -> dispose());
            
            setVisible(true);
        }

        private void saveChanges() {
            eventToEdit.setTitle(titleField.getText().trim());
            eventToEdit.setDate(dateField.getText().trim());
            eventToEdit.setTime(timeField.getText().trim());
            eventToEdit.setVenue(venueField.getText().trim());
            eventToEdit.setDescription(descArea.getText().trim());

            try {
                eventService.updateEvent(eventToEdit);
                JOptionPane.showMessageDialog(this, "Event updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                parentFrame.refreshEvents();
                dispose();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error updating event: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private static class CreateEventDialog extends JDialog {
        private final JTextField titleField = new JTextField(25);
        private final JTextField dateField = new JTextField(25);
        private final JTextField timeField = new JTextField(25);
        private final JTextField venueField = new JTextField(25);
        private final JTextArea descArea = new JTextArea(6, 25);
        private final int organizerId;
        private final EventService eventService = new EventService();
        private final DashboardFrame parent;

        public CreateEventDialog(Frame parent, int organizerId) {
            super(parent, "Create Event", true);
            this.organizerId = organizerId;
            this.parent = (DashboardFrame) parent;
            setSize(580, 580);
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

            JLabel titleLabel = new JLabel("Create New Event");
            titleLabel.setFont(ThemeUtil.HEADING_FONT);
            titleLabel.setForeground(ThemeUtil.DARK);
            titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
            gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
            cardPanel.add(titleLabel, gbc);

            gbc.gridwidth = 1;
            gbc.gridx = 0; gbc.gridy = 1;
            JLabel titleLabel2 = new JLabel("Title:");
            titleLabel2.setForeground(ThemeUtil.DARK);
            titleLabel2.setHorizontalAlignment(SwingConstants.RIGHT);
            cardPanel.add(titleLabel2, gbc);

            gbc.gridx = 1;
            ThemeUtil.styleTextField(titleField);
            cardPanel.add(titleField, gbc);

            gbc.gridx = 0; gbc.gridy = 2;
            JLabel dateLabel = new JLabel("Date (YYYY-MM-DD):");
            dateLabel.setForeground(ThemeUtil.DARK);
            dateLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            cardPanel.add(dateLabel, gbc);

            gbc.gridx = 1;
            ThemeUtil.styleTextField(dateField);
            cardPanel.add(dateField, gbc);

            gbc.gridx = 0; gbc.gridy = 3;
            JLabel timeLabel = new JLabel("Time (HH:MM):");
            timeLabel.setForeground(ThemeUtil.DARK);
            timeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            cardPanel.add(timeLabel, gbc);

            gbc.gridx = 1;
            ThemeUtil.styleTextField(timeField);
            cardPanel.add(timeField, gbc);

            gbc.gridx = 0; gbc.gridy = 4;
            JLabel venueLabel = new JLabel("Venue:");
            venueLabel.setForeground(ThemeUtil.DARK);
            venueLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            cardPanel.add(venueLabel, gbc);

            gbc.gridx = 1;
            ThemeUtil.styleTextField(venueField);
            cardPanel.add(venueField, gbc);

            gbc.gridx = 0; gbc.gridy = 5;
            JLabel descLabel = new JLabel("Description:");
            descLabel.setForeground(ThemeUtil.DARK);
            descLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            cardPanel.add(descLabel, gbc);

            gbc.gridx = 1;
            ThemeUtil.styleTextArea(descArea);
            descArea.setLineWrap(true);
            descArea.setWrapStyleWord(true);
            JScrollPane descScrollPane = new JScrollPane(descArea);
            descScrollPane.setPreferredSize(new Dimension(300, 120));
            cardPanel.add(descScrollPane, gbc);

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 0));
            buttonPanel.setBackground(Color.WHITE);
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

            JButton createBtn = new JButton("Create Event");
            JButton cancelBtn = new JButton("Cancel");
            ThemeUtil.styleAffirmativeButton(createBtn);
            ThemeUtil.styleDestructiveButton(cancelBtn);

            buttonPanel.add(createBtn);
            buttonPanel.add(cancelBtn);

            gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
            cardPanel.add(buttonPanel, gbc);

            createBtn.addActionListener(new CreateAction());
            cancelBtn.addActionListener(e -> dispose());

            setVisible(true);
        }

        private class CreateAction implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = titleField.getText().trim();
                String date = dateField.getText().trim();
                String time = timeField.getText().trim();
                String venue = venueField.getText().trim();
                String desc = descArea.getText().trim();

                if (title.isEmpty() || date.isEmpty() || time.isEmpty() || venue.isEmpty()) {
                    JOptionPane.showMessageDialog(CreateEventDialog.this, 
                        "Please fill in all required fields.", 
                        "Validation Error", 
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Event event = new Event(title, date, time, venue, organizerId, desc);
                try {
                    eventService.createEvent(event);
                    JOptionPane.showMessageDialog(CreateEventDialog.this, 
                        "Event created successfully!", 
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                    parent.refreshEvents();
                    dispose();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(CreateEventDialog.this, 
                        "Error creating event: " + ex.getMessage(), 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}
