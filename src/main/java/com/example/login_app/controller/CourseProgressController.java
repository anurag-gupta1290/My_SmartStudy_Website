package com.example.login_app.controller;

import com.example.login_app.dto.CourseProgressDTO;
import com.example.login_app.entity.Course;
import com.example.login_app.entity.CourseProgress;
import com.example.login_app.entity.User;
import com.example.login_app.repository.CourseRepository;
import com.example.login_app.repository.UserRepository;
import com.example.login_app.service.CourseProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/progress")
@CrossOrigin(origins = "http://localhost:8089", allowCredentials = "true", allowedHeaders = "*")
public class CourseProgressController {

    @Autowired
    private CourseProgressService service;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    // ================= USER DASHBOARD DATA =================
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CourseProgressDTO>> getUserProgress(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getUserProgress(userId));
    }

    // ================= ADD PROGRESS (INITIAL) =================
    @PostMapping("/add")
    public ResponseEntity<CourseProgress> addProgress(@RequestParam Long userId,
                                                      @RequestParam Long courseId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        return ResponseEntity.ok(service.addProgress(user, course));
    }

    // ================= UPDATE PROGRESS (VIDEO COMPLETE) =================
    @PostMapping("/update")
    public ResponseEntity<String> updateProgress(@RequestParam Long userId,
                                                 @RequestParam Long courseId) {

        service.incrementProgress(userId, courseId, 10.0);
        return ResponseEntity.ok("Progress updated");
    }

    // ================= LEADERBOARD =================
    @GetMapping("/leaderboard")
    public ResponseEntity<List<Map<String, Object>>> getLeaderboard() {
        List<Object[]> results = service.getLeaderboardData();
        List<Map<String, Object>> leaderboard = new ArrayList<>();

        for (Object[] row : results) {
            Map<String, Object> map = new HashMap<>();
            map.put("username", row[0]);
            // String mein "%" mat bhejo, sirf number bhejo
            map.put("progress", row[1] != null ? row[1] : 0.0);
            leaderboard.add(map);
        }
        return ResponseEntity.ok(leaderboard);
    }
}
