package com.spedex.controller;

import com.spedex.dto.*;
import com.spedex.service.PrivacyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/privacy")
public class PrivacyController {

    @Autowired
    private PrivacyService privacyService;

    // Public endpoint for viewing legal documents
    @GetMapping("/legal/{docType}")
    public ResponseEntity<LegalDocumentDto> getLegalDocument(@PathVariable String docType) {
        return ResponseEntity.ok(privacyService.getLegalDocument(docType));
    }

    // Public endpoint for guardian verification link
    @GetMapping("/guardian-consent/verify")
    public ResponseEntity<Map<String, Object>> verifyGuardianConsent(@RequestParam String token) {
        return ResponseEntity.ok(privacyService.verifyGuardianConsent(token));
    }

    // Authenticated: Get privacy settings
    @GetMapping("/settings")
    public ResponseEntity<PrivacySettingsDto> getPrivacySettings(Authentication authentication) {
        return ResponseEntity.ok(privacyService.getPrivacySettings(authentication.getName()));
    }

    // Authenticated: Update consent preferences
    @PostMapping("/consent")
    public ResponseEntity<PrivacySettingsDto> updateConsents(Authentication authentication,
                                                            @RequestBody ConsentUpdateRequestDto request) {
        return ResponseEntity.ok(privacyService.updateConsents(authentication.getName(), request));
    }

    // Authenticated: Request guardian consent (for minors)
    @PostMapping("/guardian-consent/request")
    public ResponseEntity<Map<String, Object>> requestGuardianConsent(Authentication authentication,
                                                                      @RequestBody GuardianConsentRequestDto request) {
        return ResponseEntity.ok(privacyService.requestGuardianConsent(authentication.getName(), request));
    }

    // Authenticated: Export personal data (Section 11 Right to Access)
    @GetMapping("/export")
    public ResponseEntity<UserDataExportDto> exportUserData(Authentication authentication) {
        return ResponseEntity.ok(privacyService.exportUserData(authentication.getName()));
    }

    // Authenticated: Submit privacy grievance (Section 13 Grievance Redressal)
    @PostMapping("/grievance")
    public ResponseEntity<PrivacyGrievanceDto> submitGrievance(Authentication authentication,
                                                               @RequestBody GrievanceSubmitRequestDto request) {
        return ResponseEntity.ok(privacyService.submitGrievance(authentication.getName(), request));
    }

    // Authenticated: Get user grievances
    @GetMapping("/grievance")
    public ResponseEntity<List<PrivacyGrievanceDto>> getGrievances(Authentication authentication) {
        return ResponseEntity.ok(privacyService.getGrievances(authentication.getName()));
    }

    // Authenticated: Erase personal data & anonymize account (Section 12 Right to Erasure)
    @PostMapping("/erase")
    public ResponseEntity<Map<String, Object>> eraseUserData(Authentication authentication,
                                                             @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(privacyService.eraseUserData(authentication.getName(), body));
    }

    // Authenticated: Withdraw specific consent
    @PostMapping("/consent/withdraw")
    public ResponseEntity<PrivacySettingsDto> withdrawConsent(Authentication authentication,
                                                             @RequestBody Map<String, String> body) {
        String consentType = body != null ? body.get("consentType") : null;
        return ResponseEntity.ok(privacyService.withdrawConsent(authentication.getName(), consentType));
    }

    // Authenticated: Retrieve privacy audit logs
    @GetMapping("/audit-logs")
    public ResponseEntity<List<PrivacyAuditLogDto>> getAuditLogs(Authentication authentication) {
        return ResponseEntity.ok(privacyService.getAuditLogs(authentication.getName()));
    }
}
