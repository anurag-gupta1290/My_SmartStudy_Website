package com.example.login_app.repository;

import com.example.login_app.entity.StudySession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StudySessionRepository extends JpaRepository<StudySession, Long> {

    List<StudySession> findByUserIdOrderByStartTimeDesc(Long userId);

    @Query("SELECT SUM(s.duration)/3600.0 FROM StudySession s WHERE s.user.id = :userId AND s.startTime >= :startDate")
    Double findTotalStudyHoursSince(Long userId, LocalDateTime startDate);

    @Query("SELECT s FROM StudySession s WHERE s.user.id = :userId AND s.startTime >= :startDate ORDER BY s.startTime DESC")
    List<StudySession> findRecentSessions(Long userId, LocalDateTime startDate);
}
