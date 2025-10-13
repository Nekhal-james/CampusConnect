package com.campusconnect.model;

public class Feedback {
    private int id;
    private int eventId;
    private int userId;
    private int rating;
    private String comments;

    public Feedback() {}

    public Feedback(int eventId, int userId, int rating, String comments) {
        this.eventId = eventId;
        this.userId = userId;
        this.rating = rating;
        this.comments = comments;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getEventId() { return eventId; }
    public void setEventId(int eventId) { this.eventId = eventId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }
}
