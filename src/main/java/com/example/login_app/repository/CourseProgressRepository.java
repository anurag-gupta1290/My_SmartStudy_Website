package com.example.login_app.repository;

import com.example.login_app.entity.CourseProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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



        // Top 5 Learners fetch karne ke liye (Average progress ke basis par)
        @Query("SELECT cp.user.username, AVG(cp.progressPercentage) as avgProgress " +
                "FROM CourseProgress cp " +
                "GROUP BY cp.user.id " +
                "ORDER BY avgProgress DESC")
        List<Object[]> findTopLearners();

    // ============================================================
// GLOBAL LEADERBOARD (JPQL - No more Red Errors)
// ============================================================
    /**
     * Ye query har user ka username aur unke sabhi courses ka
     * average progress calculate karke rank-wise degi.
     */
    @Query("SELECT c.user.username, AVG(c.progressPercentage) as avgProgress " +
            "FROM Course c " +
            "GROUP BY c.user.id, c.user.username " +
            "ORDER BY avgProgress DESC")
    List<Object[]> getLeaderboardData();

    // Optional: Dynamic Study Time (Total Modules * 1.5 hours)
    @Query("SELECT SUM(c.completedModules * 1.5) FROM Course c WHERE c.user.id = :userId")
    Double calculateStudyTimeByUserId(@Param("userId") Long userId);
}
