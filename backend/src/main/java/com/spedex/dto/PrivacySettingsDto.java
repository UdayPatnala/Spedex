package com.spedex.dto;

public class PrivacySettingsDto {
    public Boolean analyticsConsent;
    public Boolean marketingConsent;
    public Boolean isMinor;
    public Integer age;
    public String guardianEmail;
    public String guardianName;
    public String guardianConsentStatus;
    public Integer dataRetentionDays = 180;
    public Boolean canExport = true;
    public Boolean canRequestErasure = true;

    public PrivacySettingsDto() {}

    public PrivacySettingsDto(Boolean analyticsConsent, Boolean marketingConsent, Boolean isMinor, Integer age,
                              String guardianEmail, String guardianName, String guardianConsentStatus) {
        this.analyticsConsent = analyticsConsent;
        this.marketingConsent = marketingConsent;
        this.isMinor = isMinor;
        this.age = age;
        this.guardianEmail = guardianEmail;
        this.guardianName = guardianName;
        this.guardianConsentStatus = guardianConsentStatus;
    }
}
