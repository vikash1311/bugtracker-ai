package com.bugtracker.service;

import com.bugtracker.dto.request.CommentRequest;
import com.bugtracker.entity.Bug;
import com.bugtracker.entity.Comment;
import com.bugtracker.entity.User;
import com.bugtracker.exception.ResourceNotFoundException;
import com.bugtracker.repository.BugRepository;
import com.bugtracker.repository.CommentRepository;
import com.bugtracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BugRepository bugRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));
    }

    @Transactional
    public Comment addComment(CommentRequest request) {
        User currentUser = getCurrentUser();
        Bug bug = bugRepository.findById(request.getBugId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Bug not found"));

        Comment comment = Comment.builder()
                .content(request.getContent())
                .bug(bug)
                .user(currentUser)
                .build();

        return commentRepository.save(comment);
    }

    @Transactional
    public List<Comment> getCommentsByBug(Long bugId) {
        return commentRepository.findByBugIdOrderByCreatedAtDesc(bugId);
    }
}