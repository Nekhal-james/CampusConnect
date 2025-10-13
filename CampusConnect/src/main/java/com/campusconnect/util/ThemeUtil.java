package com.campusconnect.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;

public class ThemeUtil {
    // Color Palette
    public static final Color BACKGROUND = new Color(231, 242, 239);
    public static final Color LIGHT_ACCENT = new Color(161, 194, 189);
    public static final Color MEDIUM_ACCENT = new Color(112, 137, 147);
    public static final Color DARK = new Color(25, 24, 59);

    // Button Specific Colors
    public static final Color LOGIN_COLOR = new Color(70, 130, 180);      // Steel Blue for Login
    public static final Color SIGNUP_COLOR = new Color(46, 139, 87);     // Sea Green for Sign Up / Affirmative actions
    public static final Color DESTRUCTIVE_COLOR = new Color(199, 111, 105); // Muted Red for Unregister / Cancel

    // Fonts
    public static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 22);
    public static final Font HEADING_FONT = new Font("Arial", Font.BOLD, 16);
    public static final Font BODY_FONT = new Font("Arial", Font.PLAIN, 13);
    public static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 13);
    public static final Font NAV_FONT = new Font("Arial", Font.BOLD, 14);

    /**
     * Apply global theme settings to UIManager
     */
    public static void setAppTheme() {
        UIManager.put("Panel.background", new ColorUIResource(BACKGROUND));
        UIManager.put("Panel.foreground", new ColorUIResource(DARK));
        UIManager.put("Label.foreground", new ColorUIResource(DARK));
        UIManager.put("Label.font", new FontUIResource(BODY_FONT));
        UIManager.put("Button.background", new ColorUIResource(MEDIUM_ACCENT));
        UIManager.put("Button.foreground", new ColorUIResource(Color.WHITE));
        UIManager.put("Button.font", new FontUIResource(BUTTON_FONT));
        UIManager.put("Button.border", BorderFactory.createEmptyBorder(8, 16, 8, 16));
        UIManager.put("Button.focus", new ColorUIResource(new Color(0, 0, 0, 0)));
        UIManager.put("TextField.background", new ColorUIResource(Color.WHITE));
        UIManager.put("TextField.foreground", new ColorUIResource(DARK));
        UIManager.put("TextField.font", new FontUIResource(BODY_FONT));
        UIManager.put("TextField.border", BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LIGHT_ACCENT, 1, true),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        UIManager.put("TextArea.background", new ColorUIResource(Color.WHITE));
        UIManager.put("TextArea.foreground", new ColorUIResource(DARK));
        UIManager.put("TextArea.font", new FontUIResource(BODY_FONT));
        UIManager.put("TextArea.border", BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LIGHT_ACCENT, 1, true),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        UIManager.put("ScrollPane.background", new ColorUIResource(BACKGROUND));
        UIManager.put("ScrollPane.border", BorderFactory.createEmptyBorder());
        UIManager.put("ComboBox.background", new ColorUIResource(Color.WHITE));
        UIManager.put("ComboBox.foreground", new ColorUIResource(DARK));
        UIManager.put("ComboBox.font", new FontUIResource(BODY_FONT));
        UIManager.put("ComboBox.border", BorderFactory.createLineBorder(LIGHT_ACCENT, 1, true));
        UIManager.put("Table.background", new ColorUIResource(Color.WHITE));
        UIManager.put("Table.foreground", new ColorUIResource(DARK));
        UIManager.put("Table.font", new FontUIResource(BODY_FONT));
        UIManager.put("Table.gridColor", new ColorUIResource(LIGHT_ACCENT));
        UIManager.put("Table.selectionBackground", new ColorUIResource(MEDIUM_ACCENT));
        UIManager.put("Table.selectionForeground", new ColorUIResource(Color.WHITE));
        UIManager.put("TableHeader.background", new ColorUIResource(MEDIUM_ACCENT));
        UIManager.put("TableHeader.foreground", new ColorUIResource(Color.WHITE));
        UIManager.put("TableHeader.font", new FontUIResource(BUTTON_FONT));
        UIManager.put("OptionPane.background", new ColorUIResource(BACKGROUND));
        UIManager.put("OptionPane.messageFont", new FontUIResource(BODY_FONT));
        UIManager.put("OptionPane.buttonFont", new FontUIResource(BUTTON_FONT));
    }

    /**
     * Base style for a button (used by other methods)
     */
    private static void applyBaseButtonStyle(JButton button, Color backgroundColor, Color textColor) {
        button.setBackground(backgroundColor);
        button.setForeground(textColor);
        button.setFont(BUTTON_FONT);
        button.setBorder(BorderFactory.createEmptyBorder(12, 22, 12, 22));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setHorizontalAlignment(SwingConstants.CENTER);
    }

    /**
     * Style for affirmative actions (Register, Create, Submit)
     */
    public static void styleAffirmativeButton(JButton button) {
        applyBaseButtonStyle(button, SIGNUP_COLOR, DARK);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(34, 139, 34)); // Darker Green
                button.setForeground(Color.WHITE);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(SIGNUP_COLOR);
                button.setForeground(DARK);
            }
        });
    }

    /**
     * Style for secondary/neutral actions (Feedback, Refresh)
     */
    public static void styleSecondaryButton(JButton button) {
        applyBaseButtonStyle(button, MEDIUM_ACCENT, Color.WHITE);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(LIGHT_ACCENT);
                button.setForeground(DARK);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(MEDIUM_ACCENT);
                button.setForeground(Color.WHITE);
            }
        });
    }

    /**
     * Style for destructive actions (Unregister, Cancel, Logout)
     */
    public static void styleDestructiveButton(JButton button) {
        applyBaseButtonStyle(button, DESTRUCTIVE_COLOR, DARK);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(220, 53, 69)); // Brighter Red
                button.setForeground(Color.WHITE);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(DESTRUCTIVE_COLOR);
                button.setForeground(DARK);
            }
        });
    }

    /**
     * Style the Login button (unique style)
     */
    public static void styleLoginButton(JButton button) {
        applyBaseButtonStyle(button, LOGIN_COLOR, DARK);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(100, 149, 237));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(LOGIN_COLOR);
            }
        });
    }

    /**
     * Style the Sign Up button on the Login page (unique style)
     */
    public static void styleSignupButton(JButton button) {
        applyBaseButtonStyle(button, SIGNUP_COLOR, DARK);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(34, 139, 34));
                button.setForeground(Color.WHITE);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(SIGNUP_COLOR);
                button.setForeground(DARK);
            }
        });
    }

    /**
     * Style a navigation button
     */
    public static void styleNavButton(JButton button) {
        button.setBackground(new Color(0, 0, 0, 0));
        button.setForeground(DARK);
        button.setFont(NAV_FONT);
        button.setBorder(BorderFactory.createEmptyBorder(16, 22, 16, 22));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setContentAreaFilled(false);
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(LOGIN_COLOR);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(DARK);
            }
        });
    }

    /**
     * Style a text field
     */
    public static void styleTextField(JTextField textField) {
        textField.setBackground(Color.WHITE);
        textField.setForeground(DARK);
        textField.setFont(BODY_FONT);
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LIGHT_ACCENT, 1, true),
            BorderFactory.createEmptyBorder(14, 16, 14, 16)
        ));
        textField.setHorizontalAlignment(SwingConstants.LEFT);
    }

    /**
     * Style a text area
     */
    public static void styleTextArea(JTextArea textArea) {
        textArea.setBackground(Color.WHITE);
        textArea.setForeground(DARK);
        textArea.setFont(BODY_FONT);
        textArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LIGHT_ACCENT, 1, true),
            BorderFactory.createEmptyBorder(14, 16, 14, 16)
        ));
    }

    /**
     * Create a styled panel
     */
    public static JPanel createStyledPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LIGHT_ACCENT, 1, true),
            new EmptyBorder(30, 30, 30, 30)
        ));
        return panel;
    }

    /**
     * Create a titled panel
     */
    public static JPanel createTitledPanel(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(LIGHT_ACCENT, 1, true),
            title, 0, 0, HEADING_FONT, DARK
        ));
        return panel;
    }

    /**
     * Style a table
     */
    public static void styleTable(JTable table) {
        table.getTableHeader().setBackground(MEDIUM_ACCENT);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(BUTTON_FONT);
        table.setGridColor(LIGHT_ACCENT);
        table.setRowHeight(32);
        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : LIGHT_ACCENT);
                }
                return c;
            }
        });
    }
}
