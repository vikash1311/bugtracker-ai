package com.bugtracker.controller;

import com.bugtracker.dto.response.ApiResponse;
import com.bugtracker.entity.Bug;
import com.bugtracker.repository.BugRepository;
import com.bugtracker.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final OpenAiService openAiService;
    private final BugRepository bugRepository;

    // Feature 1 - Suggest priority
    @PostMapping("/suggest-priority")
    public ResponseEntity<ApiResponse<String>> suggestPriority(
            @RequestBody Map<String, String> body) {
        String description = body.get("description");
        String priority = openAiService.suggestPriority(description);
        return ResponseEntity.ok(
                ApiResponse.success("Priority suggested", priority));
    }

    // Feature 2 - Generate reproduction steps
    @PostMapping("/reproduction-steps")
    public ResponseEntity<ApiResponse<String>> generateSteps(
            @RequestBody Map<String, String> body) {
        String steps = openAiService.generateReproductionSteps(
                body.get("title"), body.get("description"));
        return ResponseEntity.ok(
                ApiResponse.success("Steps generated", steps));
    }

    // Feature 3 - Check duplicate
    @PostMapping("/check-duplicate")
    public ResponseEntity<ApiResponse<String>> checkDuplicate(
            @RequestBody Map<String, String> body) {
        String newDescription = body.get("description");
        Long projectId = Long.parseLong(body.get("projectId"));

        List<Bug> existingBugs = bugRepository.findByProjectId(projectId);
        String summary = existingBugs.stream()
                .map(b -> "Bug #" + b.getId() + ": " + b.getTitle()
                        + " - " + b.getDescription())
                .collect(Collectors.joining("\n"));

        String result = openAiService.checkDuplicate(newDescription, summary);
        return ResponseEntity.ok(
                ApiResponse.success("Duplicate check complete", result));
    }
}