package com.example.login_app.controller;

import com.example.login_app.entity.Course;
import com.example.login_app.entity.CourseProgress;
import com.example.login_app.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "*")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        Optional<Course> course = courseService.getCourseById(id);
        return course.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Course>> searchCourses(@RequestParam String query) {
        return ResponseEntity.ok(courseService.searchCourses(query));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Course>> getCoursesByCategory(@PathVariable String category) {
        return ResponseEntity.ok(courseService.getCoursesByCategory(category));
    }

    // ENROLL
    @PostMapping("/enroll/{userId}/{courseId}")
    public ResponseEntity<CourseProgress> enrollInCourse(
            @PathVariable Long userId,
            @PathVariable Long courseId) {
        CourseProgress progress = courseService.enrollUserInCourse(userId, courseId);
        return progress != null ? ResponseEntity.ok(progress) : ResponseEntity.badRequest().build();
    }

    // UPDATE PROGRESS
    @PutMapping("/progress/{userId}/{courseId}")
    public ResponseEntity<CourseProgress> updateProgress(
            @PathVariable Long userId,
            @PathVariable Long courseId,
            @RequestParam Integer completedModules) {
        CourseProgress progress = courseService.updateCourseProgress(userId, courseId, completedModules);
        return progress != null ? ResponseEntity.ok(progress) : ResponseEntity.notFound().build();
    }



    // UNENROLL
    @DeleteMapping("/unenroll/{userId}/{courseId}")
    public ResponseEntity<Void> unenrollFromCourse(
            @PathVariable Long userId,
            @PathVariable Long courseId) {
        boolean success = courseService.unenrollFromCourse(userId, courseId);
        return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
