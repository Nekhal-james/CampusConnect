package com.campusconnect.service;

import java.sql.SQLException;
import java.util.List;

import com.campusconnect.model.Feedback;
import com.campusconnect.repository.FeedbackRepository;

public class FeedbackService {
    private final FeedbackRepository feedbackRepository = new FeedbackRepository();

    public void submitFeedback(Feedback feedback) throws SQLException {
        feedbackRepository.save(feedback);
    }

    public List<Feedback> getFeedbackForEvent(int eventId) throws SQLException {
        return feedbackRepository.findByEventId(eventId);
    }
}
