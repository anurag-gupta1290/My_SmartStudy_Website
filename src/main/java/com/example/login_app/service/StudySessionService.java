package com.example.login_app.service;


import com.example.login_app.entity.StudySession;
import com.example.login_app.entity.User;
import com.example.login_app.repository.StudySessionRepository;
import com.example.login_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StudySessionService {

    @Autowired
    private StudySessionRepository studySessionRepository;

    @Autowired
    private UserRepository userRepository;

    public StudySession startStudySession(Long userId, String sessionType, String title, String courseName) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            StudySession session = new StudySession(userOpt.get(), sessionType, title, courseName);
            return studySessionRepository.save(session);
        }
        return null;
    }

    public StudySession endStudySession(Long sessionId) {
        return studySessionRepository.findById(sessionId).map(session -> {
            session.setEndTime(LocalDateTime.now());
            session.setCompleted(true);
            return studySessionRepository.save(session);
        }).orElse(null);
    }

    public List<StudySession> getUserStudySessions(Long userId) {
        return studySessionRepository.findByUserIdOrderByStartTimeDesc(userId);
    }

    public Double getTotalStudyHours(Long userId, int days) {
        LocalDateTime startDate = LocalDateTime.now().minusDays(days);
        Double totalHours = studySessionRepository.findTotalStudyHoursSince(userId, startDate);
        return totalHours != null ? totalHours : 0.0;
    }
}