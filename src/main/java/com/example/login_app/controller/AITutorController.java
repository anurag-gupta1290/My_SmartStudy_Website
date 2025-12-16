package com.example.login_app.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api/ai-tutor")
@CrossOrigin(origins = "*")
public class AITutorController {

    private final String[] responses = {
            "Great question! Let me break this down for you step by step...",
            "I'd be happy to help with that concept. Here's a detailed explanation...",
            "That's an excellent question! This is fundamental to understanding the topic...",
            "Let me provide you with a comprehensive answer with examples...",
            "I understand you're asking about this. Here are the key points you need to know..."
    };

    @PostMapping("/ask")
    public ResponseEntity<Map<String, String>> askQuestion(@RequestBody Map<String, String> request) {
        String question = request.get("question");
        Random random = new Random();
        String response = responses[random.nextInt(responses.length)];

        Map<String, String> result = new HashMap<>();
        result.put("question", question);
        result.put("response", response);
        result.put("timestamp", java.time.LocalDateTime.now().toString());

        return ResponseEntity.ok(result);
    }

    @GetMapping("/quick-questions")
    public ResponseEntity<Map<String, String>> getQuickQuestions() {
        Map<String, String> questions = new HashMap<>();
        questions.put("machine_learning", "Explain machine learning in simple terms");
        questions.put("javascript", "Help with JavaScript closures");
        questions.put("study_tips", "Effective study tips for coding");
        questions.put("project_ideas", "Web development project ideas");

        return ResponseEntity.ok(questions);
    }
}