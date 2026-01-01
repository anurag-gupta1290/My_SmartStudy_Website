package com.example.login_app.service;

import com.example.login_app.dto.DashboardStatsDTO;
import com.example.login_app.entity.Course;
import com.example.login_app.entity.Assignment;
import com.example.login_app.entity.CourseProgress;
import com.example.login_app.entity.StudySession;
import com.example.login_app.repository.CourseRepository;
import com.example.login_app.repository.AssignmentRepository;
import com.example.login_app.repository.CourseProgressRepository;
import com.example.login_app.repository.StudySessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class DashboardService {

    @Autowired
    private CourseProgressRepository courseProgressRepository;

    @Autowired
    private StudySessionRepository studySessionRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    public DashboardStatsDTO getDashboardStats(Long userId) {
        DashboardStatsDTO stats = new DashboardStatsDTO();
        stats.setActiveCourses(8);
        stats.setOverallProgress(84.0);  // Double value
        stats.setStudyTimeHours(18.5);   // Double value
        stats.setAiTutorStatus("24/7");  // aiTutorAvailable → aiTutorStatus
        return stats;
    }

    public List<CourseProgress> getUserCourseProgress(Long userId) {
        List<CourseProgress> progressList = new ArrayList<>();

        // Sample course 1
        CourseProgress progress1 = new CourseProgress();
        progress1.setId(1L);
        progress1.setProgressPercentage(65.0);
        progress1.setCompletedModules(12);


        Course course1 = new Course();
        course1.setId(1L);
        course1.setTitle("Full Stack Web Development");
        course1.setDescription("Master HTML, CSS, JavaScript, React, Node.js, MongoDB");
        course1.setCategory("Development");
        course1.setInstructor("Sarah Johnson");
        progress1.setCourse(course1);
        progressList.add(progress1);

        // Sample course 2
        CourseProgress progress2 = new CourseProgress();
        progress2.setId(2L);
        progress2.setProgressPercentage(42.0);
        progress2.setCompletedModules(8);


        Course course2 = new Course();
        course2.setId(2L);
        course2.setTitle("Machine Learning & AI");
        course2.setDescription("Python, TensorFlow, Neural Networks, Data Analysis");
        course2.setCategory("Data Science");
        course2.setInstructor("Dr. Alex Chen");
        progress2.setCourse(course2);
        progressList.add(progress2);

        // Sample course 3
        CourseProgress progress3 = new CourseProgress();
        progress3.setId(3L);
        progress3.setProgressPercentage(78.0);
        progress3.setCompletedModules(14);


        Course course3 = new Course();
        course3.setId(3L);
        course3.setTitle("Flutter App Development");
        course3.setDescription("Build cross-platform iOS & Android apps with Dart");
        course3.setCategory("Mobile");
        course3.setInstructor("Mike Thompson");
        progress3.setCourse(course3);
        progressList.add(progress3);

        return progressList;
    }

    public List<Course> getUserCourses(Long userId) {
        List<Course> courses = new ArrayList<>();

        Course course1 = new Course();
        course1.setId(1L);
        course1.setTitle("Full Stack Web Development");
        course1.setDescription("Master HTML, CSS, JavaScript, React, Node.js, MongoDB");
        course1.setCategory("Development");
        course1.setInstructor("Sarah Johnson");
        course1.setTotalModules(18);
        course1.setCompletedModules(12);
        course1.setProgressPercentage(65.0);
        courses.add(course1);

        Course course2 = new Course();
        course2.setId(2L);
        course2.setTitle("Machine Learning & AI");
        course2.setDescription("Python, TensorFlow, Neural Networks, Data Analysis");
        course2.setCategory("Data Science");
        course2.setInstructor("Dr. Alex Chen");
        course2.setTotalModules(19);
        course2.setCompletedModules(8);
        course2.setProgressPercentage(42.0);
        courses.add(course2);

        Course course3 = new Course();
        course3.setId(3L);
        course3.setTitle("Flutter App Development");
        course3.setDescription("Build cross-platform iOS & Android apps with Dart");
        course3.setCategory("Mobile");
        course3.setInstructor("Mike Thompson");
        course3.setTotalModules(18);
        course3.setCompletedModules(14);
        course3.setProgressPercentage(78.0);
        courses.add(course3);

        return courses;
    }

    public List<Assignment> getUserAssignments(Long userId) {
        List<Assignment> assignments = new ArrayList<>();

        // FIXED: Using default constructor instead of parameterized constructor
        Assignment assignment1 = new Assignment();
        assignment1.setId(1L);
        assignment1.setTitle("Java Spring Boot Project");
        assignment1.setDescription("Create REST API with authentication & database");
        assignment1.setStatus("IN_PROGRESS");
        assignment1.setDueDate(LocalDateTime.now().plusDays(1));
        assignments.add(assignment1);

        Assignment assignment2 = new Assignment();
        assignment2.setId(2L);
        assignment2.setTitle("Machine Learning Report");
        assignment2.setDescription("Analyze dataset and build prediction model");
        assignment2.setStatus("PENDING");
        assignment2.setDueDate(LocalDateTime.now().plusDays(3));
        assignments.add(assignment2);

        Assignment assignment3 = new Assignment();
        assignment3.setId(3L);
        assignment3.setTitle("Web Development Portfolio");
        assignment3.setDescription("Build responsive portfolio website with 5 pages");
        assignment3.setStatus("COMPLETED");
        assignment3.setDueDate(LocalDateTime.now().minusDays(2));
        assignments.add(assignment3);

        return assignments;
    }

    public Map<String, Object> getUserProgress(Long userId) {
        Map<String, Object> progressData = new HashMap<>();

        progressData.put("studyTime", "18.5h");
        progressData.put("dailyAverage", "2.6h");
        progressData.put("bestDay", "4.2 hours (Monday)");
        progressData.put("courseProgress", getUserCourseProgress(userId));

        return progressData;
    }

    public List<StudySession> getRecentStudySessions(Long userId, int days) {
        return new ArrayList<>();
    }

    public List<Object> getRecentAchievements(Long userId) {
        List<Object> achievements = new ArrayList<>();

        Map<String, String> achievement1 = new HashMap<>();
        achievement1.put("title", "Course Master!");
        achievement1.put("description", "Completed Web Dev Module 3");
        achievement1.put("type", "success");
        achievements.add(achievement1);

        Map<String, String> achievement2 = new HashMap<>();
        achievement2.put("title", "7-Day Streak!");
        achievement2.put("description", "Keep up the great work");
        achievement2.put("type", "info");
        achievements.add(achievement2);

        return achievements;
    }
}