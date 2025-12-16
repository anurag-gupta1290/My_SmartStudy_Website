package com.example.login_app.repository;

import com.example.login_app.entity.CourseProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseProgressRepository extends JpaRepository<CourseProgress, Long> {

    List<CourseProgress> findByUserId(Long userId);

    Optional<CourseProgress> findByUserIdAndCourseId(Long userId, Long courseId);

    List<CourseProgress> findByUserIdAndIsActiveTrue(Long userId);

    @Query("SELECT AVG(cp.progressPercentage) FROM CourseProgress cp WHERE cp.user.id = :userId AND cp.isActive = true")
    Double findAverageProgressByUserId(Long userId);

    @Query("SELECT COUNT(cp) FROM CourseProgress cp WHERE cp.user.id = :userId AND cp.isActive = true")
    Integer countActiveCoursesByUserId(Long userId);
}
