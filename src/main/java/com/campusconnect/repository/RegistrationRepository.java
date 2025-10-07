package com.campusconnect.repository;

import com.campusconnect.model.Registration;
import com.campusconnect.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RegistrationRepository {
    public void save(Registration reg) throws SQLException {
        String sql = "INSERT INTO registrations (event_id, user_id, timestamp) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, reg.getEventId());
            stmt.setInt(2, reg.getUserId());
            stmt.setString(3, reg.getTimestamp());
            stmt.executeUpdate();
        }
    }

    public List<Registration> findByUserId(int userId) throws SQLException {
        List<Registration> regs = new ArrayList<>();
        String sql = "SELECT * FROM registrations WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Registration reg = new Registration();
                reg.setId(rs.getInt("id"));
                reg.setEventId(rs.getInt("event_id"));
                reg.setUserId(rs.getInt("user_id"));
                reg.setTimestamp(rs.getString("timestamp"));
                regs.add(reg);
            }
        }
        return regs;
    }

    public void deleteByEventAndUser(int eventId, int userId) throws SQLException {
        String sql = "DELETE FROM registrations WHERE event_id = ? AND user_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, eventId);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        }
    }
}
