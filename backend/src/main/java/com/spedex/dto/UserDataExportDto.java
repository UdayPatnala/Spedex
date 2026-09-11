package com.spedex.dto;

import java.time.LocalDateTime;
import java.util.List;

public class UserDataExportDto {
    public LocalDateTime exportTimestamp = LocalDateTime.now();
    public SpedexUserDto userData;
    public PrivacySettingsDto privacySettings;
    public List<TripDto> trips;
    public List<ConsentRecordDto> consentHistory;
    public List<PrivacyGrievanceDto> grievanceHistory;
    public String complianceNotice = "Exported in accordance with DPDP Act 2023 Section 11 (Right to Access Personal Data).";

    public UserDataExportDto() {}
}
