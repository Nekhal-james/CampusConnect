package com.campusconnect.repository;

import com.campusconnect.model.Feedback;
import com.campusconnect.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FeedbackRepository {
    public void save(Feedback feedback) throws SQLException {
        String sql = "INSERT INTO feedback (event_id, user_id, rating, comments) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, feedback.getEventId());
            stmt.setInt(2, feedback.getUserId());
            stmt.setInt(3, feedback.getRating());
            stmt.setString(4, feedback.getComments());
            stmt.executeUpdate();
        }
    }

    public List<Feedback> findByEventId(int eventId) throws SQLException {
        List<Feedback> feedbacks = new ArrayList<>();
        String sql = "SELECT * FROM feedback WHERE event_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, eventId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Feedback fb = new Feedback();
                fb.setId(rs.getInt("id"));
                fb.setEventId(rs.getInt("event_id"));
                fb.setUserId(rs.getInt("user_id"));
                fb.setRating(rs.getInt("rating"));
                fb.setComments(rs.getString("comments"));
                feedbacks.add(fb);
            }
        }
        return feedbacks;
    }
}
