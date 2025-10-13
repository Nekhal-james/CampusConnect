package com.campusconnect.model;

public class Event {
    private int id;
    private String title;
    private String date;
    private String time;
    private String venue;
    private int organizerId;
    private String description;

    public Event() {}

    public Event(String title, String date, String time, String venue, int organizerId, String description) {
        this.title = title;
        this.date = date;
        this.time = time;
        this.venue = venue;
        this.organizerId = organizerId;
        this.description = description;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }

    public int getOrganizerId() { return organizerId; }
    public void setOrganizerId(int organizerId) { this.organizerId = organizerId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
