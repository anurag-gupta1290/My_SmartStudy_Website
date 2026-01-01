package com.example.login_app.repository;

import com.example.login_app.entity.FlashCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlashCardRepository extends JpaRepository<FlashCard, Long> {

    // Get all flashcards for a specific user
    List<FlashCard> findByUserId(Long userId);
}
