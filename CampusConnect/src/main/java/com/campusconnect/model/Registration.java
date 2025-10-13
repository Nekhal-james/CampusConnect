package com.campusconnect.model;

public class Registration {
    private int id;
    private int eventId;
    private int userId;
    private String timestamp;

    public Registration() {}

    public Registration(int eventId, int userId, String timestamp) {
        this.eventId = eventId;
        this.userId = userId;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getEventId() { return eventId; }
    public void setEventId(int eventId) { this.eventId = eventId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}
