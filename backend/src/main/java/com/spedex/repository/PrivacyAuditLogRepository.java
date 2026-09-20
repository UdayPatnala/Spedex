package com.spedex.repository;

import com.spedex.model.PrivacyAuditLog;
import com.spedex.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrivacyAuditLogRepository extends JpaRepository<PrivacyAuditLog, Long> {
    List<PrivacyAuditLog> findByUserEmailOrderByTimestampDesc(String userEmail);
    List<PrivacyAuditLog> findByUserOrderByTimestampDesc(User user);
}
