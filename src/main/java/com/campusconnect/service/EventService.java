package com.campusconnect.service;

import com.campusconnect.model.Event;
import com.campusconnect.repository.EventRepository;
import java.sql.SQLException;
import java.util.List;

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
}
