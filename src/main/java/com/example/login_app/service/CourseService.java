package com.example.login_app.service;

import com.example.login_app.entity.Course;
import com.example.login_app.entity.CourseProgress;
import com.example.login_app.entity.User;
import com.example.login_app.repository.CourseProgressRepository;
import com.example.login_app.repository.CourseRepository;
import com.example.login_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseProgressRepository courseProgressRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    public List<Course> searchCourses(String query) {
        return courseRepository.searchCourses(query);
    }

    public List<Course> getCoursesByCategory(String category) {
        return courseRepository.findByCategory(category);
    }

    // ENROLL USER IN COURSE
    public CourseProgress enrollUserInCourse(Long userId, Long courseId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Course> courseOpt = courseRepository.findById(courseId);

        if (userOpt.isPresent() && courseOpt.isPresent()) {
            Optional<CourseProgress> existingProgress =
                    courseProgressRepository.findByUserIdAndCourseId(userId, courseId);

            if (existingProgress.isPresent()) {
                CourseProgress progress = existingProgress.get();
                progress.setActive(true); // reactivate if previously unenrolled
                return courseProgressRepository.save(progress);
            }

            CourseProgress progress = new CourseProgress(userOpt.get(), courseOpt.get());
            return courseProgressRepository.save(progress);
        }
        return null;
    }

    // UPDATE PROGRESS
    public CourseProgress updateCourseProgress(Long userId, Long courseId, Integer completedModules) {
        Optional<CourseProgress> progressOpt =
                courseProgressRepository.findByUserIdAndCourseId(userId, courseId);

        if (progressOpt.isPresent()) {
            CourseProgress progress = progressOpt.get();
            progress.setCompletedModules(completedModules);
            return courseProgressRepository.save(progress);
        }
        return null;
    }

    // UNENROLL USER
    public boolean unenrollFromCourse(Long userId, Long courseId) {
        Optional<CourseProgress> progressOpt =
                courseProgressRepository.findByUserIdAndCourseId(userId, courseId);

        if (progressOpt.isPresent()) {
            CourseProgress progress = progressOpt.get();
            progress.setActive(false);
            courseProgressRepository.save(progress);
            return true;
        }
        return false;
    }
}
