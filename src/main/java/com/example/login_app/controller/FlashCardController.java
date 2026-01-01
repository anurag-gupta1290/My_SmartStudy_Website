package com.example.login_app.controller;

import com.example.login_app.entity.FlashCard;
import com.example.login_app.service.FlashCardService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flashcards")
@CrossOrigin
public class FlashCardController {

    private final FlashCardService service;

    public FlashCardController(FlashCardService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping
    public FlashCard create(@RequestBody FlashCard card) {
        System.out.println("🔥 API HIT: " + card.getQuestion());
        return service.create(card);
    }

    // READ
    @GetMapping("/{userId}")
    public List<FlashCard> getAll(@PathVariable Long userId) {
        return service.getAllByUser(userId);
    }

    // UPDATE
    @PutMapping("/{id}")
    public FlashCard update(@PathVariable Long id, @RequestBody FlashCard card) {
        return service.update(id, card);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

}
