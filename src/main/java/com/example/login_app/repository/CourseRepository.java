package com.example.login_app.repository;

import com.example.login_app.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    // Find courses by user ID
    List<Course> findByUserId(Long userId);

    // Find courses by category
    List<Course> findByCategory(String category);

    // Find active courses (with progress > 0)
    @Query("SELECT c FROM Course c WHERE c.user.id = :userId AND c.progressPercentage > 0")
    List<Course> findActiveCoursesByUserId(@Param("userId") Long userId);

    // Search courses by title (case-insensitive) - FIXED METHOD NAME
    @Query("SELECT c FROM Course c WHERE LOWER(c.title) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Course> searchCourses(@Param("query") String query);

    // Find courses by instructor
    List<Course> findByInstructor(String instructor);

    // Find courses by difficulty level
    List<Course> findByDifficultyLevel(String difficultyLevel);

    // Get course count by user
    @Query("SELECT COUNT(c) FROM Course c WHERE c.user.id = :userId")
    Long countByUserId(@Param("userId") Long userId);

    // Get average progress by user
    @Query("SELECT AVG(c.progressPercentage) FROM Course c WHERE c.user.id = :userId")
    Double findAverageProgressByUserId(@Param("userId") Long userId);



    // JPQL Query: Username aur Average Progress nikalne ke liye
    @Query("SELECT c.user.username, AVG(c.progressPercentage) " +
            "FROM Course c " +
            "GROUP BY c.user.id, c.user.username " +
            "ORDER BY AVG(c.progressPercentage) DESC")
    List<Object[]> getLeaderboardData();

    // Dynamic Study Time calculation logic
    @Query("SELECT SUM(c.completedModules * 1.5) FROM Course c WHERE c.user.id = :userId")
    Double calculateStudyTimeByUserId(@Param("userId") Long userId);

}