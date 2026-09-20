package com.spedex.dto;

import java.time.LocalDateTime;

public class PrivacyAuditLogDto {
    public Long id;
    public String userEmail;
    public String eventType;
    public String description;
    public String ipAddress;
    public String userAgent;
    public LocalDateTime timestamp;

    public PrivacyAuditLogDto() {}

    public PrivacyAuditLogDto(Long id, String userEmail, String eventType, String description,
                              String ipAddress, String userAgent, LocalDateTime timestamp) {
        this.id = id;
        this.userEmail = userEmail;
        this.eventType = eventType;
        this.description = description;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.timestamp = timestamp;
    }
}
