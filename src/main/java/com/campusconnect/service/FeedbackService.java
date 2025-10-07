package com.campusconnect.service;

import com.campusconnect.model.Feedback;
import com.campusconnect.repository.FeedbackRepository;
import java.sql.SQLException;
import java.util.List;

public class FeedbackService {
    private final FeedbackRepository feedbackRepository = new FeedbackRepository();

    public void submitFeedback(Feedback feedback) throws SQLException {
        feedbackRepository.save(feedback);
    }

    public List<Feedback> getFeedbackForEvent(int eventId) throws SQLException {
        return feedbackRepository.findByEventId(eventId);
    }
}
