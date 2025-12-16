package com.example.login_app.controller;


import com.example.login_app.entity.StudySession;
import com.example.login_app.service.StudySessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
@CrossOrigin(origins = "*")
public class StudySessionController {

    @Autowired
    private StudySessionService studySessionService;

    @PostMapping("/start/{userId}")
    public ResponseEntity<StudySession> startSession(
            @PathVariable Long userId,
            @RequestParam String sessionType,
            @RequestParam String title,
            @RequestParam(required = false) String courseName) {
        StudySession session = studySessionService.startStudySession(userId, sessionType, title, courseName);
        return session != null ? ResponseEntity.ok(session) : ResponseEntity.badRequest().build();
    }

    @PutMapping("/end/{sessionId}")
    public ResponseEntity<StudySession> endSession(@PathVariable Long sessionId) {
        StudySession session = studySessionService.endStudySession(sessionId);
        return session != null ? ResponseEntity.ok(session) : ResponseEntity.notFound().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<StudySession>> getUserSessions(@PathVariable Long userId) {
        List<StudySession> sessions = studySessionService.getUserStudySessions(userId);
        return ResponseEntity.ok(sessions);
    }

    @GetMapping("/user/{userId}/hours")
    public ResponseEntity<Double> getStudyHours(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "7") int days) {
        Double hours = studySessionService.getTotalStudyHours(userId, days);
        return ResponseEntity.ok(hours);
    }
}