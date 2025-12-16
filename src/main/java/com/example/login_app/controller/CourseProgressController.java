package com.example.login_app.controller;


import com.example.login_app.entity.Course;
import com.example.login_app.entity.CourseProgress;
import com.example.login_app.entity.User;
import com.example.login_app.repository.CourseRepository;
import com.example.login_app.repository.UserRepository;
import com.example.login_app.service.CourseProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/progress")
public class CourseProgressController {

    @Autowired
    private CourseProgressService service;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @PostMapping("/add")
    public CourseProgress addProgress(@RequestParam Long userId, @RequestParam Long courseId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        return service.addProgress(user, course);
    }
}
