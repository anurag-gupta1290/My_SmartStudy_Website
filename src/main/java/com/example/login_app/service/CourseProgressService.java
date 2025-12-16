package com.example.login_app.service;


import com.example.login_app.entity.Course;
import com.example.login_app.entity.CourseProgress;
import com.example.login_app.entity.User;
import com.example.login_app.repository.CourseProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseProgressService {

    @Autowired
    private CourseProgressRepository progressRepository;

    public CourseProgress addProgress(User user, Course course) {
        CourseProgress progress = new CourseProgress(user, course);
        return progressRepository.save(progress);
    }
}
