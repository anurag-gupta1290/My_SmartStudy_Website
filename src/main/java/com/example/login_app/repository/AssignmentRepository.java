package com.example.login_app.repository;

import com.example.login_app.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    List<Assignment> findByUserIdOrderByDueDateAsc(Long userId);

    List<Assignment> findByUserIdAndStatus(Long userId, String status);

    @Query("SELECT a FROM Assignment a WHERE a.user.id = :userId AND a.dueDate BETWEEN :start AND :end")
    List<Assignment> findUpcomingAssignments(Long userId, LocalDateTime start, LocalDateTime end);
}
