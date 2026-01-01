package com.example.login_app.service;

import com.example.login_app.entity.FlashCard;
import com.example.login_app.repository.FlashCardRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlashCardService {

    private final FlashCardRepository repository;

    public FlashCardService(FlashCardRepository repository) {
        this.repository = repository;
    }

    // CREATE
    public FlashCard create(FlashCard card) {

        System.out.println("🔥 SERVICE CREATE HIT");
        System.out.println("Question: " + card.getQuestion());
        System.out.println("Answer: " + card.getAnswer());
        System.out.println("UserId: " + card.getUserId());

        if (card.getQuestion() == null || card.getQuestion().isBlank()
                || card.getAnswer() == null || card.getAnswer().isBlank()) {
            throw new IllegalArgumentException("Question or Answer cannot be empty");
        }

        return repository.save(card);
    }

    // READ
    public List<FlashCard> getAllByUser(Long userId) {
        return repository.findByUserId(userId);
    }

    // UPDATE
    public FlashCard update(Long id, FlashCard updated) {

        FlashCard card = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("FlashCard not found"));

        card.setQuestion(updated.getQuestion());
        card.setAnswer(updated.getAnswer());

        return repository.save(card);
    }

    // DELETE
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
