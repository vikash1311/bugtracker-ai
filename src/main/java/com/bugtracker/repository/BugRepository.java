package com.bugtracker.repository;

import com.bugtracker.entity.Bug;
import com.bugtracker.enums.BugStatus;
import com.bugtracker.enums.Priority;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BugRepository extends JpaRepository<Bug, Long> {
    Page<Bug> findByProjectId(Long projectId, Pageable pageable);
    Page<Bug> findByAssignedToId(Long userId, Pageable pageable);
    Page<Bug> findByProjectIdAndStatus(Long projectId, BugStatus status, Pageable pageable);
    Page<Bug> findByProjectIdAndPriority(Long projectId, Priority priority, Pageable pageable);
    List<Bug> findByProjectId(Long projectId);
    long countByProjectIdAndStatus(Long projectId, BugStatus status);
}