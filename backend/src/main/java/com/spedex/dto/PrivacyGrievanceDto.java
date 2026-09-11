package com.spedex.dto;

import java.time.LocalDateTime;

public class PrivacyGrievanceDto {
    public String ticketId;
    public String category;
    public String description;
    public String status;
    public LocalDateTime createdAt;
    public LocalDateTime redressedAt;
    public String responseMessage;

    public PrivacyGrievanceDto() {}

    public PrivacyGrievanceDto(String ticketId, String category, String description, String status,
                               LocalDateTime createdAt, LocalDateTime redressedAt, String responseMessage) {
        this.ticketId = ticketId;
        this.category = category;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.redressedAt = redressedAt;
        this.responseMessage = responseMessage;
    }
}
