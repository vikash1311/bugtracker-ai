package com.bugtracker.service;

import com.bugtracker.dto.request.BugRequest;
import com.bugtracker.dto.response.BugResponse;
import com.bugtracker.entity.ActivityLog;
import com.bugtracker.entity.Bug;
import com.bugtracker.entity.Project;
import com.bugtracker.entity.User;
import com.bugtracker.enums.BugStatus;
import com.bugtracker.enums.Priority;
import com.bugtracker.exception.ResourceNotFoundException;
import com.bugtracker.repository.ActivityLogRepository;
import com.bugtracker.repository.BugRepository;
import com.bugtracker.repository.ProjectRepository;
import com.bugtracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BugService {

    private final BugRepository bugRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ActivityLogRepository activityLogRepository;

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));
    }

    private BugResponse mapToResponse(Bug bug) {
        return BugResponse.builder()
                .id(bug.getId())
                .title(bug.getTitle())
                .description(bug.getDescription())
                .status(bug.getStatus())
                .priority(bug.getPriority())
                .projectName(bug.getProject().getName())
                .reportedByName(bug.getReportedBy().getName())
                .assignedToName(bug.getAssignedTo() != null
                        ? bug.getAssignedTo().getName() : "Unassigned")
                .createdAt(bug.getCreatedAt())
                .updatedAt(bug.getUpdatedAt())
                .build();
    }

    @Transactional
    public BugResponse createBug(BugRequest request) {
        User currentUser = getCurrentUser();

        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Project not found"));

        User assignedTo = null;
        if (request.getAssignedToId() != null) {
            assignedTo = userRepository.findById(request.getAssignedToId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Assigned user not found"));
        }

        Bug bug = Bug.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(BugStatus.OPEN)
                .priority(request.getPriority())
                .project(project)
                .reportedBy(currentUser)
                .assignedTo(assignedTo)
                .build();

        Bug saved = bugRepository.save(bug);
        logActivity(saved, currentUser, "BUG_CREATED", null, "OPEN");
        return mapToResponse(saved);
    }

    @Transactional
    public Page<BugResponse> getBugsByProject(Long projectId,
                                              BugStatus status, Priority priority, int page, int size) {
        Pageable pageable = PageRequest.of(page, size,
                Sort.by("createdAt").descending());

        Page<Bug> bugs;
        if (status != null) {
            bugs = bugRepository.findByProjectIdAndStatus(
                    projectId, status, pageable);
        } else if (priority != null) {
            bugs = bugRepository.findByProjectIdAndPriority(
                    projectId, priority, pageable);
        } else {
            bugs = bugRepository.findByProjectId(projectId, pageable);
        }

        return bugs.map(this::mapToResponse);
    }

    @Transactional
    public BugResponse getBugById(Long id) {
        Bug bug = bugRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Bug not found with id: " + id));
        return mapToResponse(bug);
    }

    @Transactional
    public BugResponse updateBug(Long id, BugRequest request) {
        User currentUser = getCurrentUser();
        Bug bug = bugRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Bug not found"));

        String oldStatus = bug.getStatus().name();
        bug.setTitle(request.getTitle());
        bug.setDescription(request.getDescription());
        bug.setPriority(request.getPriority());

        if (request.getAssignedToId() != null) {
            User assignedTo = userRepository
                    .findById(request.getAssignedToId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("User not found"));
            bug.setAssignedTo(assignedTo);
        }

        Bug updated = bugRepository.save(bug);
        logActivity(updated, currentUser, "BUG_UPDATED",
                oldStatus, updated.getStatus().name());
        return mapToResponse(updated);
    }

    @Transactional
    public BugResponse updateBugStatus(Long id, BugStatus newStatus) {
        User currentUser = getCurrentUser();
        Bug bug = bugRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Bug not found"));

        String oldStatus = bug.getStatus().name();
        bug.setStatus(newStatus);
        Bug updated = bugRepository.save(bug);

        logActivity(updated, currentUser, "STATUS_CHANGED",
                oldStatus, newStatus.name());
        return mapToResponse(updated);
    }

    @Transactional
    public void deleteBug(Long id) {
        Bug bug = bugRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Bug not found"));
        bugRepository.delete(bug);
    }

    private void logActivity(Bug bug, User user,
                             String action, String oldValue, String newValue) {
        ActivityLog log = ActivityLog.builder()
                .bug(bug)
                .user(user)
                .action(action)
                .oldValue(oldValue)
                .newValue(newValue)
                .build();
        activityLogRepository.save(log);
    }
}