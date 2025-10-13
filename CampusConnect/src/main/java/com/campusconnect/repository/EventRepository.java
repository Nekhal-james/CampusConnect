package com.campusconnect.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.campusconnect.model.Event;
import com.campusconnect.util.DBConnection;

public class EventRepository {
    public void save(Event event) throws SQLException {
        String sql = "INSERT INTO events (title, date, time, venue, organizer_id, description) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, event.getTitle());
            stmt.setString(2, event.getDate());
            stmt.setString(3, event.getTime());
            stmt.setString(4, event.getVenue());
            stmt.setInt(5, event.getOrganizerId());
            stmt.setString(6, event.getDescription());
            stmt.executeUpdate();
        }
    }

    public List<Event> findAll() throws SQLException {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM events";
        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Event event = new Event();
                event.setId(rs.getInt("id"));
                event.setTitle(rs.getString("title"));
                event.setDate(rs.getString("date"));
                event.setTime(rs.getString("time"));
                event.setVenue(rs.getString("venue"));
                event.setOrganizerId(rs.getInt("organizer_id"));
                event.setDescription(rs.getString("description"));
                events.add(event);
            }
        }
        return events;
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM events WHERE id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
    
    public void update(Event event) throws SQLException {
        String sql = "UPDATE events SET title = ?, date = ?, time = ?, venue = ?, description = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, event.getTitle());
            stmt.setString(2, event.getDate());
            stmt.setString(3, event.getTime());
            stmt.setString(4, event.getVenue());
            stmt.setString(5, event.getDescription());
            stmt.setInt(6, event.getId());
            stmt.executeUpdate();
        }
    }
}
