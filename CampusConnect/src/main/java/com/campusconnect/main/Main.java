package com.campusconnect.main;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.campusconnect.ui.LoginFrame;
import com.campusconnect.util.DBConnection;
import com.campusconnect.util.ThemeUtil;

public class Main {
    public static void main(String[] args) {
        // Apply theme before creating any UI components
        ThemeUtil.setAppTheme();
        
        // Set system look and feel for better integration
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        DBConnection.initDB();
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
