package com.campusconnect.service;

import java.sql.SQLException;
import java.util.List;

import com.campusconnect.model.Event;
import com.campusconnect.repository.EventRepository;

public class EventService {
    private final EventRepository eventRepository = new EventRepository();

    public void createEvent(Event event) throws SQLException {
        eventRepository.save(event);
    }

    public List<Event> getAllEvents() throws SQLException {
        return eventRepository.findAll();
    }

    public void deleteEvent(int id) throws SQLException {
        eventRepository.delete(id);
    }
    
    public void updateEvent(Event event) throws SQLException {
        eventRepository.update(event);
    }
}
