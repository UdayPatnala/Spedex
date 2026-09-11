package com.spedex.dto;

import java.time.LocalDateTime;

public class ConsentRecordDto {
    public Long id;
    public String consentType;
    public String action;
    public LocalDateTime timestamp;
    public String policyVersion;
    public String details;

    public ConsentRecordDto() {}

    public ConsentRecordDto(Long id, String consentType, String action, LocalDateTime timestamp, String policyVersion, String details) {
        this.id = id;
        this.consentType = consentType;
        this.action = action;
        this.timestamp = timestamp;
        this.policyVersion = policyVersion;
        this.details = details;
    }
}
