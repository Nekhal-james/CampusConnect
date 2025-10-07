package com.campusconnect.main;

import com.campusconnect.ui.LoginFrame;
import com.campusconnect.util.DBConnection;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        DBConnection.initDB();
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
