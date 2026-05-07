package com.bugtracker.controller;

import com.bugtracker.dto.request.CommentRequest;
import com.bugtracker.dto.response.ApiResponse;
import com.bugtracker.entity.Comment;
import com.bugtracker.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<ApiResponse<Comment>> addComment(
            @Valid @RequestBody CommentRequest request) {
        Comment comment = commentService.addComment(request);
        return ResponseEntity.ok(
                ApiResponse.success("Comment added", comment));
    }

    @GetMapping("/bug/{bugId}")
    public ResponseEntity<ApiResponse<List<Comment>>> getComments(
            @PathVariable Long bugId) {
        List<Comment> comments = commentService.getCommentsByBug(bugId);
        return ResponseEntity.ok(
                ApiResponse.success("Comments fetched", comments));
    }
}