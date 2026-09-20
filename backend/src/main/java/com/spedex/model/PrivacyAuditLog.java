package com.spedex.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "privacy_audit_logs")
public class PrivacyAuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @Column(nullable = false)
    private String userEmail;

    @Column(nullable = false)
    private String eventType; // e.g. "ACCOUNT_CREATED", "AGE_CATEGORY_ASSIGNED", "GUARDIAN_CONSENT_REQUESTED", "PAYMENT_ACTION_BLOCKED", "DATA_EXPORT_REQUESTED", "ACCOUNT_DELETION_REQUESTED", "CONSENT_UPDATED", "CONSENT_WITHDRAWN"

    @Column(length = 1000)
    private String description;

    private String ipAddress;

    private String userAgent;

    @Column(nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();

    public PrivacyAuditLog() {}

    public PrivacyAuditLog(User user, String userEmail, String eventType, String description) {
        this.user = user;
        this.userEmail = userEmail != null ? userEmail : (user != null ? user.getEmail() : "anonymous");
        this.eventType = eventType;
        this.description = description;
        this.timestamp = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public String getUserAgent() { return userAgent; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
