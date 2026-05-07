package com.bugtracker.dto.response;

import com.bugtracker.enums.BugStatus;
import com.bugtracker.enums.Priority;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BugResponse {
    private Long id;
    private String title;
    private String description;
    private BugStatus status;
    private Priority priority;
    private String projectName;
    private String reportedByName;
    private String assignedToName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}