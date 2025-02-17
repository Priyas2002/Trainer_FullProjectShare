package com.ems.repository;

import com.ems.entity.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
    
    @Query("SELECT a FROM ActivityLog a ORDER BY a.timestamp DESC LIMIT 6")
    List<ActivityLog> findLatestActivities();
}
