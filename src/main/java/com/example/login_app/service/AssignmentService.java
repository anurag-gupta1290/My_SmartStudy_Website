package com.example.login_app.service;


import com.example.login_app.entity.Assignment;
import com.example.login_app.entity.User;
import com.example.login_app.repository.AssignmentRepository;
import com.example.login_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Assignment> getUserAssignments(Long userId) {
        return assignmentRepository.findByUserIdOrderByDueDateAsc(userId);
    }

    public List<Assignment> getAssignmentsByStatus(Long userId, String status) {
        return assignmentRepository.findByUserIdAndStatus(userId, status);
    }

    public List<Assignment> getUpcomingAssignments(Long userId, int days) {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusDays(days);
        return assignmentRepository.findUpcomingAssignments(userId, start, end);
    }

    public Assignment createAssignment(Long userId, Assignment assignment) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            assignment.setUser(userOpt.get());
            return assignmentRepository.save(assignment);
        }
        return null;
    }

    public Assignment updateAssignment(Long assignmentId, Assignment assignmentDetails) {
        return assignmentRepository.findById(assignmentId).map(assignment -> {
            assignment.setTitle(assignmentDetails.getTitle());
            assignment.setDescription(assignmentDetails.getDescription());
            assignment.setStatus(assignmentDetails.getStatus());
            assignment.setDueDate(assignmentDetails.getDueDate());
            assignment.setMaxPoints(assignmentDetails.getMaxPoints());
            return assignmentRepository.save(assignment);
        }).orElse(null);
    }

    public Assignment submitAssignment(Long assignmentId) {
        return assignmentRepository.findById(assignmentId).map(assignment -> {
            assignment.setStatus("Submitted");
            assignment.setSubmittedAt(LocalDateTime.now());
            return assignmentRepository.save(assignment);
        }).orElse(null);
    }

    public boolean deleteAssignment(Long assignmentId) {
        if (assignmentRepository.existsById(assignmentId)) {
            assignmentRepository.deleteById(assignmentId);
            return true;
        }
        return false;
    }
}