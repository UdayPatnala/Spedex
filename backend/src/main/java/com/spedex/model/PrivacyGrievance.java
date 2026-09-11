package com.spedex.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "privacy_grievances")
public class PrivacyGrievance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String ticketId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String category; // e.g. "ACCESS_REQUEST", "ERASURE_REQUEST", "CONSENT_REVOCATION", "DATA_CORRECTION", "GENERAL_GRIEVANCE"

    @Column(length = 2000, nullable = false)
    private String description;

    @Column(nullable = false)
    private String status = "OPEN"; // "OPEN", "UNDER_REVIEW", "RESOLVED", "REJECTED"

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime redressedAt;

    @Column(length = 2000)
    private String responseMessage;

    public PrivacyGrievance() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTicketId() { return ticketId; }
    public void setTicketId(String ticketId) { this.ticketId = ticketId; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getRedressedAt() { return redressedAt; }
    public void setRedressedAt(LocalDateTime redressedAt) { this.redressedAt = redressedAt; }
    public String getResponseMessage() { return responseMessage; }
    public void setResponseMessage(String responseMessage) { this.responseMessage = responseMessage; }
}
