package com.bugtracker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class OpenAiService {

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Value("${openai.model}")
    private String model;

    private final RestTemplate restTemplate;

    public String askAI(String systemPrompt, String userMessage) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> systemMsg = new HashMap<>();
        systemMsg.put("role", "system");
        systemMsg.put("content", systemPrompt);

        Map<String, Object> userMsg = new HashMap<>();
        userMsg.put("role", "user");
        userMsg.put("content", userMessage);

        Map<String, Object> body = new HashMap<>();
        body.put("model", model);
        body.put("messages", List.of(systemMsg, userMsg));
        body.put("max_tokens", 500);
        body.put("temperature", 0.3);

        HttpEntity<Map<String, Object>> entity =
                new HttpEntity<>(body, headers);

        try {
            System.out.println("Calling AI URL: " + apiUrl);
            System.out.println("Model: " + model);
            System.out.println("API Key starts with: " +
                    (apiKey != null ? apiKey.substring(0, 10) : "NULL"));

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    apiUrl, entity, Map.class);

            System.out.println("AI Response status: " +
                    response.getStatusCode());

            List<Map> choices = (List<Map>) response.getBody().get("choices");
            Map message = (Map) choices.get(0).get("message");
            return (String) message.get("content");

        } catch (Exception e) {
            System.out.println("OpenAI Error TYPE: " +
                    e.getClass().getName());
            System.out.println("OpenAI Error MESSAGE: " + e.getMessage());
            if (e.getCause() != null) {
                System.out.println("OpenAI Error CAUSE: " +
                        e.getCause().getMessage());
            }
            return "AI service unavailable: " + e.getMessage();
        }
    }

    // Feature 1 - Suggest priority from description
    public String suggestPriority(String description) {
        String system = """
            You are a bug triage assistant.
            Given a bug description, respond with ONLY one word.
            Choose exactly one from: LOW, MEDIUM, HIGH, CRITICAL
            No punctuation. No explanation. Just the single word.
            """;
        String result = askAI(system, description).trim().toUpperCase();
        // Extract only the valid priority word
        if (result.contains("CRITICAL")) return "CRITICAL";
        if (result.contains("HIGH")) return "HIGH";
        if (result.contains("MEDIUM")) return "MEDIUM";
        return "LOW";
    }

    // Feature 2 - Generate reproduction steps
    public String generateReproductionSteps(String title, String description) {
        String system = """
            You are a senior QA engineer.
            Given a bug title and description, generate clear numbered
            reproduction steps for developers.
            Be concise and technical. Maximum 6 steps.
            """;
        return askAI(system,
                "Title: " + title + "\nDescription: " + description);
    }

    // Feature 3 - Check for duplicate bug
    public String checkDuplicate(String newDescription,
                                 String existingBugsSummary) {
        String system = """
            You are a bug deduplication assistant.
            Given a new bug description and a list of existing bugs,
            respond with either:
            - "DUPLICATE: Bug #<id> - <reason>" if similar bug exists
            - "UNIQUE" if no similar bug found
            Only respond with one of these formats.
            """;
        return askAI(system,
                "New bug: " + newDescription +
                        "\n\nExisting bugs:\n" + existingBugsSummary);
    }
}