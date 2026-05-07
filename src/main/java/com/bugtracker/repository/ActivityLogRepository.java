package com.bugtracker.repository;

import com.bugtracker.entity.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
    List<ActivityLog> findByBugIdOrderByCreatedAtDesc(Long bugId);
    List<ActivityLog> findByUserIdOrderByCreatedAtDesc(Long userId);
}