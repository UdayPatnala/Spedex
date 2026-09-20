package com.spedex.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SpedexUserDto {
    public Long id;
    public String name;
    public String email;
    public String plan;
    @JsonProperty("avatar_initials")
    public String avatarInitials;
    @JsonProperty("member_since")
    public String memberSince; // ISO string
    @JsonProperty("profile_picture_url")
    public String profilePictureUrl;

    @JsonProperty("is_minor")
    public Boolean isMinor;
    public Integer age;
    @JsonProperty("guardian_email")
    public String guardianEmail;
    @JsonProperty("guardian_name")
    public String guardianName;
    @JsonProperty("guardian_consent_status")
    public String guardianConsentStatus;
    @JsonProperty("analytics_consent")
    public Boolean analyticsConsent;
    @JsonProperty("marketing_consent")
    public Boolean marketingConsent;
    @JsonProperty("location_consent")
    public Boolean locationConsent;
    @JsonProperty("ai_consent")
    public Boolean aiConsent;
}
