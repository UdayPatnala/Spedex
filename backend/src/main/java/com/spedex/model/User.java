package com.spedex.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    private String plan = "Premium";
    private String avatarInitials = "AL";
    private String profilePictureUrl;
    private LocalDateTime memberSince = LocalDateTime.now();

    // DPDP Act 2023 Compliance Fields
    private Boolean isMinor = false;
    private Integer age = 18;
    private String guardianEmail;
    private String guardianName;
    private String guardianConsentStatus = "NOT_REQUIRED"; // "NOT_REQUIRED", "PENDING", "VERIFIED", "REJECTED"
    private Boolean analyticsConsent = false;
    private Boolean marketingConsent = false;
    private Boolean locationConsent = false;
    private Boolean aiConsent = false;
    private Boolean isErased = false;
    private LocalDateTime erasedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PrivacyAuditLog> privacyAuditLogs;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vendor> vendors;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Budget> budgets;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reminder> reminders;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Trip> trips;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConsentRecord> consentRecords;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PrivacyGrievance> privacyGrievances;

    public User() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getPlan() { return plan; }
    public void setPlan(String plan) { this.plan = plan; }
    public String getAvatarInitials() { return avatarInitials; }
    public void setAvatarInitials(String avatarInitials) { this.avatarInitials = avatarInitials; }
    public String getProfilePictureUrl() { return profilePictureUrl; }
    public void setProfilePictureUrl(String profilePictureUrl) { this.profilePictureUrl = profilePictureUrl; }
    public LocalDateTime getMemberSince() { return memberSince; }
    public void setMemberSince(LocalDateTime memberSince) { this.memberSince = memberSince; }

    public Boolean getIsMinor() { return isMinor != null && isMinor; }
    public void setIsMinor(Boolean isMinor) { this.isMinor = isMinor; }
    public Integer getAge() { return age != null ? age : 18; }
    public void setAge(Integer age) { this.age = age; }
    public String getGuardianEmail() { return guardianEmail; }
    public void setGuardianEmail(String guardianEmail) { this.guardianEmail = guardianEmail; }
    public String getGuardianName() { return guardianName; }
    public void setGuardianName(String guardianName) { this.guardianName = guardianName; }
    public String getGuardianConsentStatus() { return guardianConsentStatus != null ? guardianConsentStatus : "NOT_REQUIRED"; }
    public void setGuardianConsentStatus(String guardianConsentStatus) { this.guardianConsentStatus = guardianConsentStatus; }
    public Boolean getAnalyticsConsent() { return analyticsConsent != null && analyticsConsent; }
    public void setAnalyticsConsent(Boolean analyticsConsent) { this.analyticsConsent = analyticsConsent; }
    public Boolean getMarketingConsent() { return marketingConsent != null && marketingConsent; }
    public void setMarketingConsent(Boolean marketingConsent) { this.marketingConsent = marketingConsent; }
    public Boolean getLocationConsent() { return locationConsent != null && locationConsent; }
    public void setLocationConsent(Boolean locationConsent) { this.locationConsent = locationConsent; }
    public Boolean getAiConsent() { return aiConsent != null && aiConsent; }
    public void setAiConsent(Boolean aiConsent) { this.aiConsent = aiConsent; }
    public Boolean getIsErased() { return isErased != null && isErased; }
    public void setIsErased(Boolean isErased) { this.isErased = isErased; }
    public LocalDateTime getErasedAt() { return erasedAt; }
    public void setErasedAt(LocalDateTime erasedAt) { this.erasedAt = erasedAt; }

    public List<Vendor> getVendors() { return vendors; }
    public void setVendors(List<Vendor> vendors) { this.vendors = vendors; }
    public List<Budget> getBudgets() { return budgets; }
    public void setBudgets(List<Budget> budgets) { this.budgets = budgets; }
    public List<Reminder> getReminders() { return reminders; }
    public void setReminders(List<Reminder> reminders) { this.reminders = reminders; }
    public List<Transaction> getTransactions() { return transactions; }
    public void setTransactions(List<Transaction> transactions) { this.transactions = transactions; }
    public List<Trip> getTrips() { return trips; }
    public void setTrips(List<Trip> trips) { this.trips = trips; }
    public List<ConsentRecord> getConsentRecords() { return consentRecords; }
    public void setConsentRecords(List<ConsentRecord> consentRecords) { this.consentRecords = consentRecords; }
    public List<PrivacyGrievance> getPrivacyGrievances() { return privacyGrievances; }
    public void setPrivacyGrievances(List<PrivacyGrievance> privacyGrievances) { this.privacyGrievances = privacyGrievances; }
    public List<PrivacyAuditLog> getPrivacyAuditLogs() { return privacyAuditLogs; }
    public void setPrivacyAuditLogs(List<PrivacyAuditLog> privacyAuditLogs) { this.privacyAuditLogs = privacyAuditLogs; }
}
