package com.example.login_app.controller;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/ai-tutor")
@CrossOrigin(origins = "*")
public class AITutorController {

    @Value("${ai.gemini.key:AIzaSyATUen1VJd7bn5kqIr805QqZaiEHMGWqBM}")
    private String geminiApiKey;

    private static final String GEMINI_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=";

    // Conversation store (in-memory)
    private final Map<String, List<Map<String, String>>> conversationStore = new HashMap<>();
    @PostConstruct
    public void checkKey() {
        System.out.println("Gemini API Key = " + geminiApiKey);
    }
    /* ================= ASK AI ================= */
    @PostMapping("/ask")
    public ResponseEntity<Map<String, Object>> askQuestion(
            @RequestBody Map<String, String> request,
            @RequestHeader(value = "session-id", required = false) String sessionId) {

        String question = request.get("question");
        if (question == null || question.trim().isEmpty()) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", "Question is empty");
            return ResponseEntity.badRequest().body(error);
        }

        String userId = request.get("userId") != null ? request.get("userId") :
                (sessionId != null ? sessionId : "default-user");

        Map<String, Object> response = new HashMap<>();
        response.put("question", question);
        response.put("timestamp", LocalDateTime.now().toString());

        try {
            String aiAnswer = callGeminiSafe(question, userId);
            response.put("response", aiAnswer);
            response.put("source", "GEMINI_AI");
            response.put("success", true);

            saveConversation(userId, question, aiAnswer);

        } catch (Exception e) {
            response.put("response", getFallbackResponse(question));
            response.put("source", "FALLBACK");
            response.put("success", false);
            response.put("error", e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    /* ================= GET CONVERSATION HISTORY ================= */
    @GetMapping("/history/{userId}")
    public ResponseEntity<List<Map<String, String>>> getHistory(@PathVariable String userId) {
        List<Map<String, String>> history = conversationStore.getOrDefault(userId, new ArrayList<>());
        return ResponseEntity.ok(history);
    }

    /* ================= QUICK QUESTIONS ================= */
    @GetMapping("/quick-questions")
    public ResponseEntity<Map<String, String>> getQuickQuestions() {
        Map<String, String> questions = new LinkedHashMap<>();
        questions.put("machine_learning", "Explain machine learning in simple terms");
        questions.put("javascript", "Explain JavaScript closures with example");
        questions.put("study_tips", "Effective study tips for coding students");
        questions.put("project_ideas", "Web development project ideas for students");
        questions.put("html_css", "Difference between HTML and CSS with examples");
        questions.put("react_basics", "What is React and why use it?");
        return ResponseEntity.ok(questions);
    }

    /* ================= SUMMARIZE ================= */
    @PostMapping("/summarize")
    public ResponseEntity<Map<String, Object>> summarizeConversation(@RequestBody Map<String, String> request) {

        String userId = request.get("userId");
        List<Map<String, String>> history = conversationStore.getOrDefault(userId, new ArrayList<>());

        if (history.isEmpty()) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "No conversation history found");
            error.put("success", false);
            return ResponseEntity.badRequest().body(error);
        }

        // Convert conversation to text
        StringBuilder conversationText = new StringBuilder();
        for (Map<String, String> msg : history) {
            conversationText.append(msg.get("role")).append(": ").append(msg.get("content")).append("\n");
        }

        try {
            String summary = callGeminiSafe(
                    "Summarize this study conversation in 3-5 key points:\n\n" + conversationText.toString(),
                    userId
            );

            Map<String, Object> response = new HashMap<>();
            response.put("summary", summary);
            response.put("success", true);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Fallback summary
            Map<String, Object> fallback = new HashMap<>();
            fallback.put("summary", "AI service unavailable. Cannot summarize now.");
            fallback.put("success", false);
            return ResponseEntity.ok(fallback);
        }
    }

    /* ================= GEMINI CALL (SAFE) ================= */
    private String callGeminiSafe(String question, String contextId) throws Exception {
        if (geminiApiKey == null || geminiApiKey.isEmpty() || geminiApiKey.contains("YourAPIKey")) {
            throw new Exception("Gemini API key is not configured.");
        }

        RestTemplate restTemplate = new RestTemplate();

        // Build conversation context
        List<Map<String, String>> history = conversationStore.getOrDefault(contextId, new ArrayList<>());
        StringBuilder context = new StringBuilder();
        if (!history.isEmpty()) {
            List<Map<String, String>> recentHistory = history.subList(Math.max(0, history.size() - 3), history.size());
            context.append("Previous conversation context:\n");
            for (Map<String, String> msg : recentHistory) {
                context.append(msg.get("role")).append(": ").append(msg.get("content")).append("\n");
            }
            context.append("\nNow answer this new question:\n");
        }

        Map<String, Object> requestBody = new HashMap<>();
        Map<String, Object> content = new HashMap<>();
        List<Map<String, Object>> parts = new ArrayList<>();

        Map<String, Object> textPart = new HashMap<>();
        textPart.put("text", context +
                "You are an AI study tutor. Answer in a helpful, detailed, and educational manner. " +
                "Provide examples when relevant. Keep response under 800 tokens.\n\n" +
                "Student's question: " + question);

        parts.add(textPart);
        content.put("parts", parts);
        requestBody.put("contents", Collections.singletonList(content));
        requestBody.put("generationConfig", Map.of("temperature", 0.7, "maxOutputTokens", 800));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        String apiUrl = GEMINI_URL + geminiApiKey;

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Object candidatesObj = response.getBody().get("candidates");
                if (candidatesObj instanceof List<?> candidatesList && !candidatesList.isEmpty()) {
                    Object firstCandidateObj = candidatesList.get(0);
                    if (firstCandidateObj instanceof Map<?, ?> firstCandidate) {
                        Object contentObj = firstCandidate.get("content");
                        if (contentObj instanceof Map<?, ?> contentMap) {
                            Object partsObj = contentMap.get("parts");
                            if (partsObj instanceof List<?> partsList && !partsList.isEmpty()) {
                                Object firstPartObj = partsList.get(0);
                                if (firstPartObj instanceof Map<?, ?> firstPart) {
                                    Object text = firstPart.get("text");
                                    if (text instanceof String textStr) {
                                        return textStr;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            throw new Exception("Invalid response from Gemini API");

        } catch (Exception e) {
            System.err.println("Gemini API Error: " + e.getMessage());
            throw e;
        }
    }

    /* ================= CONVERSATION STORAGE ================= */
    private void saveConversation(String userId, String question, String answer) {
        List<Map<String, String>> userHistory = conversationStore.getOrDefault(userId, new ArrayList<>());

        Map<String, String> userMessage = Map.of(
                "role", "user",
                "content", question,
                "timestamp", LocalDateTime.now().toString()
        );
        Map<String, String> aiMessage = Map.of(
                "role", "assistant",
                "content", answer,
                "timestamp", LocalDateTime.now().toString()
        );

        userHistory = new ArrayList<>(userHistory); // prevent sublist issues
        userHistory.add(userMessage);
        userHistory.add(aiMessage);

        if (userHistory.size() > 20) {
            userHistory = new ArrayList<>(userHistory.subList(userHistory.size() - 20, userHistory.size()));
        }

        conversationStore.put(userId, userHistory);
    }

    /* ================= FALLBACK RESPONSE ================= */
    private String getFallbackResponse(String question) {
        return "I'm having trouble connecting to the AI service right now. "
                + "But based on your question about **" + question + "**, here's what I suggest:\n\n"
                + "1. **Break it down**: Start with the basic concepts\n"
                + "2. **Find examples**: Look for practical implementations\n"
                + "3. **Practice**: Try writing small code snippets\n"
                + "4. **Ask specifics**: What exactly are you finding difficult?\n\n"
                + "Try asking again in a moment when the AI service is back online.";
    }

    /* ================= HEALTH CHECK ================= */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "AI Tutor");
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("aiProvider", "Google Gemini");

        if (geminiApiKey == null || geminiApiKey.isEmpty() || geminiApiKey.contains("YourAPIKey")) {
            response.put("aiStatus", "NOT_CONFIGURED");
            response.put("message", "Gemini API key not configured");
        } else {
            response.put("aiStatus", "CONFIGURED");
            response.put("message", "AI service ready");
        }

        return ResponseEntity.ok(response);
    }
}
