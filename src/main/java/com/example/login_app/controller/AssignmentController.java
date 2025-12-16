package com.example.login_app.controller;


import com.example.login_app.entity.Assignment;
import com.example.login_app.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assignments")
@CrossOrigin(origins = "*")
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Assignment>> getUserAssignments(@PathVariable Long userId) {
        List<Assignment> assignments = assignmentService.getUserAssignments(userId);
        return ResponseEntity.ok(assignments);
    }

    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<List<Assignment>> getAssignmentsByStatus(
            @PathVariable Long userId,
            @PathVariable String status) {
        List<Assignment> assignments = assignmentService.getAssignmentsByStatus(userId, status);
        return ResponseEntity.ok(assignments);
    }

    @GetMapping("/user/{userId}/upcoming")
    public ResponseEntity<List<Assignment>> getUpcomingAssignments(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "7") int days) {
        List<Assignment> assignments = assignmentService.getUpcomingAssignments(userId, days);
        return ResponseEntity.ok(assignments);
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<Assignment> createAssignment(
            @PathVariable Long userId,
            @RequestBody Assignment assignment) {
        Assignment created = assignmentService.createAssignment(userId, assignment);
        return created != null ? ResponseEntity.ok(created) : ResponseEntity.badRequest().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Assignment> updateAssignment(
            @PathVariable Long id,
            @RequestBody Assignment assignment) {
        Assignment updated = assignmentService.updateAssignment(id, assignment);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/submit")
    public ResponseEntity<Assignment> submitAssignment(@PathVariable Long id) {
        Assignment submitted = assignmentService.submitAssignment(id);
        return submitted != null ? ResponseEntity.ok(submitted) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Long id) {
        boolean success = assignmentService.deleteAssignment(id);
        return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}