package com.campusconnect.ui;

import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;

public class NotificationManager {
    private final Timer timer = new Timer();

    public void scheduleNotification(String message, long delay) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, message, "Reminder", JOptionPane.INFORMATION_MESSAGE));
            }
        }, delay);
    }

    public void cancelAll() {
        timer.cancel();
    }
}
