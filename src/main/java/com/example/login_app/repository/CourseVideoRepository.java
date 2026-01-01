package com.example.login_app.repository;

import com.example.login_app.entity.CourseVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseVideoRepository extends JpaRepository<CourseVideo, Long> {
    // fetch all videos for a course
    List<CourseVideo> findByCourseId(Long courseId);

}
