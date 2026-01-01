package com.example.login_app.service;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import com.example.login_app.entity.Course;
import com.example.login_app.entity.CourseProgress;
import com.example.login_app.entity.User;
import com.example.login_app.repository.CourseProgressRepository;
import com.example.login_app.repository.CourseRepository;
import com.example.login_app.repository.UserRepository;
import com.example.login_app.dto.CourseProgressDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourseProgressService {

    @Autowired
    private CourseProgressRepository courseProgressRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    // 1. DYNAMIC DASHBOARD DATA (Automatic Initialization)
    public List<CourseProgressDTO> getUserProgress(Long userId) {
        // Step A: Check karo user ki koi progress entry hai?
        List<CourseProgress> progressList = courseProgressRepository.findByUserId(userId);

        // Step B: AGAR DATA NAHI HAI -> TOH AUTO-CREATE KARO
        if (progressList.isEmpty()) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found: " + userId));

            // Saare courses fetch karo jo database mein hain
            List<Course> allCourses = courseRepository.findAll();

            // Har course ke liye 0% progress wali entry banao (Auto-Insert)
            for (Course course : allCourses) {
                CourseProgress newCp = new CourseProgress();
                newCp.setUser(user);
                newCp.setCourse(course);
                newCp.setProgressPercentage(0.0);
                newCp.setActive(true);
                courseProgressRepository.save(newCp);
            }
            // Data insert hone ke baad dobara list fetch karo
            progressList = courseProgressRepository.findByUserId(userId);
        }

        // Step C: DTO mein convert karke return karo
        return progressList.stream()
                .map(cp -> new CourseProgressDTO(
                        cp.getCourse().getId(),
                        cp.getCourse().getTitle(),
                        cp.getProgressPercentage()
                )).collect(Collectors.toList());
    }

    // 2. VIDEO END HONE PAR PROGRESS UPDATE (Secure & Capped at 100%)
    @Transactional
    public void incrementProgress(Long userId, Long courseId, Double increment) {
        courseProgressRepository.findByUserIdAndCourseId(userId, courseId)
                .ifPresentOrElse(progress -> {
                    double current = progress.getProgressPercentage();
                    double updated = Math.min(100.0, current + increment);
                    progress.setProgressPercentage(updated);
                    courseProgressRepository.save(progress);
                    System.out.println("✅ Success: Progress for User " + userId + " updated to " + updated + "%");
                }, () -> {
                    System.out.println("⚠️ Warning: No progress record found for Course ID " + courseId);
                });
    }

    // 3. LEADERBOARD DATA
    public List<Object[]> getLeaderboardData() {
        return courseProgressRepository.findTopLearners();
    }

    // 4. ADD PROGRESS (Utility method agar manually call karna ho)
    public CourseProgress addProgress(User user, Course course) {
        return courseProgressRepository.findByUserIdAndCourseId(user.getId(), course.getId())
                .orElseGet(() -> {
                    CourseProgress newProgress = new CourseProgress();
                    newProgress.setUser(user);
                    newProgress.setCourse(course);
                    newProgress.setProgressPercentage(0.0);
                    newProgress.setActive(true);
                    return courseProgressRepository.save(newProgress);
                });
    }
}