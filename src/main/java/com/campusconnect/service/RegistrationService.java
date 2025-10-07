package com.campusconnect.service;

import com.campusconnect.model.Registration;
import com.campusconnect.repository.RegistrationRepository;
import java.sql.SQLException;
import java.util.List;

public class RegistrationService {
    private final RegistrationRepository registrationRepository = new RegistrationRepository();

    public void register(int eventId, int userId, String timestamp) throws SQLException {
        Registration reg = new Registration(eventId, userId, timestamp);
        registrationRepository.save(reg);
    }

    public List<Registration> getUserRegistrations(int userId) throws SQLException {
        return registrationRepository.findByUserId(userId);
    }

    public void unregister(int eventId, int userId) throws SQLException {
        registrationRepository.deleteByEventAndUser(eventId, userId);
    }
}
