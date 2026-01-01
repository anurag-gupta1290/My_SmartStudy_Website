package com.example.login_app.repository;

import com.example.login_app.entity.UserProgress; // Sahi entity import karein
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserProgressRepository extends JpaRepository<UserProgress, Long> {

    // ERROR FIX: Service layer is method ko dhoond rahi hai
    long countByUserIdAndVideoIdInAndCompletedTrue(Long userId, List<Long> videoIds);

    // Agar aapko purane methods chahiye toh unhe UserProgress ke hisaab se likhna hoga
    List<UserProgress> findByUserId(Long userId);
}