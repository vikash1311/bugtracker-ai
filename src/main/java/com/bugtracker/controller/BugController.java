package com.bugtracker.controller;

import com.bugtracker.dto.request.BugRequest;
import com.bugtracker.dto.response.ApiResponse;
import com.bugtracker.dto.response.BugResponse;
import com.bugtracker.enums.BugStatus;
import com.bugtracker.enums.Priority;
import com.bugtracker.service.BugService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bugs")
@RequiredArgsConstructor
public class BugController {

    private final BugService bugService;

    @PostMapping
    public ResponseEntity<ApiResponse<BugResponse>> createBug(
            @Valid @RequestBody BugRequest request) {
        BugResponse response = bugService.createBug(request);
        return ResponseEntity.ok(
                ApiResponse.success("Bug created", response));
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<ApiResponse<Page<BugResponse>>> getBugsByProject(
            @PathVariable Long projectId,
            @RequestParam(required = false) BugStatus status,
            @RequestParam(required = false) Priority priority,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<BugResponse> bugs = bugService.getBugsByProject(
                projectId, status, priority, page, size);
        return ResponseEntity.ok(
                ApiResponse.success("Bugs fetched", bugs));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BugResponse>> getBug(
            @PathVariable Long id) {
        BugResponse response = bugService.getBugById(id);
        return ResponseEntity.ok(
                ApiResponse.success("Bug fetched", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BugResponse>> updateBug(
            @PathVariable Long id,
            @Valid @RequestBody BugRequest request) {
        BugResponse response = bugService.updateBug(id, request);
        return ResponseEntity.ok(
                ApiResponse.success("Bug updated", response));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<BugResponse>> updateStatus(
            @PathVariable Long id,
            @RequestParam BugStatus status) {
        BugResponse response = bugService.updateBugStatus(id, status);
        return ResponseEntity.ok(
                ApiResponse.success("Status updated", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBug(
            @PathVariable Long id) {
        bugService.deleteBug(id);
        return ResponseEntity.ok(
                ApiResponse.success("Bug deleted", null));
    }
}