package com.bugtracker.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ProjectResponse {
    private Long id;
    private String name;
    private String description;
    private String createdByName;
    private String createdByEmail;
    private LocalDateTime createdAt;
}