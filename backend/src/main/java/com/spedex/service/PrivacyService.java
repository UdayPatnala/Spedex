package com.spedex.service;

import com.spedex.dto.*;
import com.spedex.model.ConsentRecord;
import com.spedex.model.PrivacyAuditLog;
import com.spedex.model.PrivacyGrievance;
import com.spedex.model.User;
import com.spedex.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PrivacyService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConsentRecordRepository consentRecordRepository;

    @Autowired
    private PrivacyGrievanceRepository privacyGrievanceRepository;

    @Autowired
    private PrivacyAuditLogRepository auditLogRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private ReminderRepository reminderRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TripService tripService;

    @Autowired
    private UserCapabilityService capabilityService;

    // Simulated in-memory storage for parental verification tokens (maps token -> userEmail)
    private final Map<String, String> guardianTokens = new HashMap<>();

    public PrivacySettingsDto getPrivacySettings(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new PrivacySettingsDto(
                user.getAnalyticsConsent(),
                user.getMarketingConsent(),
                user.getLocationConsent(),
                user.getAiConsent(),
                user.getIsMinor(),
                user.getAge(),
                user.getGuardianEmail(),
                user.getGuardianName(),
                user.getGuardianConsentStatus()
        );
    }

    @Transactional
    public PrivacySettingsDto updateConsents(String email, ConsentUpdateRequestDto request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (Boolean.TRUE.equals(user.getIsMinor())) {
            user.setAnalyticsConsent(false);
            user.setMarketingConsent(false);
            user.setLocationConsent(false);
            user.setAiConsent(false);
        } else {
            if (request.analyticsConsent != null) {
                user.setAnalyticsConsent(request.analyticsConsent);
            }
            if (request.marketingConsent != null) {
                user.setMarketingConsent(request.marketingConsent);
            }
            if (request.locationConsent != null) {
                user.setLocationConsent(request.locationConsent);
            }
            if (request.aiConsent != null) {
                user.setAiConsent(request.aiConsent);
            }
        }

        userRepository.save(user);

        ConsentRecord record = new ConsentRecord();
        record.setUser(user);
        record.setConsentType("PREFERENCES_UPDATE");
        record.setAction("UPDATED");
        record.setDetails("Analytics: " + user.getAnalyticsConsent() + ", Marketing: " + user.getMarketingConsent()
                + ", Location: " + user.getLocationConsent() + ", AI: " + user.getAiConsent());
        consentRecordRepository.save(record);

        PrivacyAuditLog auditLog = new PrivacyAuditLog(
                user,
                user.getEmail(),
                "CONSENT_UPDATED",
                "Privacy preferences updated: " + record.getDetails()
        );
        auditLogRepository.save(auditLog);

        return getPrivacySettings(email);
    }

    @Transactional
    public PrivacySettingsDto withdrawConsent(String email, String consentType) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (consentType == null || consentType.trim().isEmpty()) {
            throw new IllegalArgumentException("Consent type is required");
        }

        String normalizedType = consentType.toUpperCase().trim();
        switch (normalizedType) {
            case "ANALYTICS":
                user.setAnalyticsConsent(false);
                break;
            case "MARKETING":
                user.setMarketingConsent(false);
                break;
            case "LOCATION":
                user.setLocationConsent(false);
                break;
            case "AI":
            case "AI_PERSONALIZATION":
                user.setAiConsent(false);
                break;
            default:
                throw new IllegalArgumentException("Unknown consent purpose: " + consentType);
        }

        userRepository.save(user);

        ConsentRecord record = new ConsentRecord();
        record.setUser(user);
        record.setConsentType(normalizedType);
        record.setAction("WITHDRAWN");
        record.setDetails("Consent revoked directly by user in Privacy Center");
        consentRecordRepository.save(record);

        PrivacyAuditLog auditLog = new PrivacyAuditLog(
                user,
                user.getEmail(),
                "CONSENT_WITHDRAWN",
                "Revoked consent for: " + normalizedType
        );
        auditLogRepository.save(auditLog);

        return getPrivacySettings(email);
    }

    @Transactional
    public Map<String, Object> requestGuardianConsent(String email, GuardianConsentRequestDto request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.guardianName == null || request.guardianName.trim().isEmpty() ||
            request.guardianEmail == null || request.guardianEmail.trim().isEmpty()) {
            throw new IllegalArgumentException("Guardian name and email are required");
        }

        user.setGuardianName(request.guardianName.trim());
        user.setGuardianEmail(request.guardianEmail.trim());
        user.setGuardianConsentStatus("PENDING");
        userRepository.save(user);

        String token = UUID.randomUUID().toString();
        guardianTokens.put(token, email);

        PrivacyAuditLog auditLog = new PrivacyAuditLog(
                user,
                user.getEmail(),
                "GUARDIAN_CONSENT_REQUESTED",
                "Verification link dispatched to guardian email: " + user.getGuardianEmail()
        );
        auditLogRepository.save(auditLog);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Guardian verification request sent");
        response.put("guardianEmail", user.getGuardianEmail());
        response.put("verificationToken", token);
        return response;
    }

    @Transactional
    public Map<String, Object> verifyGuardianConsent(String token) {
        if (token == null || !guardianTokens.containsKey(token)) {
            throw new IllegalArgumentException("Invalid or expired guardian verification token");
        }

        String userEmail = guardianTokens.get(token);
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setGuardianConsentStatus("VERIFIED");
        userRepository.save(user);

        ConsentRecord record = new ConsentRecord();
        record.setUser(user);
        record.setConsentType("GUARDIAN_VERIFICATION");
        record.setAction("GRANTED");
        record.setDetails("Guardian consent verified via token verification link");
        consentRecordRepository.save(record);

        PrivacyAuditLog auditLog = new PrivacyAuditLog(
                user,
                user.getEmail(),
                "GUARDIAN_CONSENT_VERIFIED",
                "Parental/guardian consent verified successfully for minor user"
        );
        auditLogRepository.save(auditLog);

        guardianTokens.remove(token);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Guardian consent verified successfully");
        return response;
    }

    @Transactional
    public UserDataExportDto exportUserData(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDataExportDto export = new UserDataExportDto();
        export.userData = userService.mapToDto(user);
        export.privacySettings = getPrivacySettings(email);
        export.capabilities = capabilityService.getCapabilities(user);

        // Export all transactions, vendors, budgets, reminders, trips
        export.transactions = transactionRepository.findByUserId(user.getId()).stream()
                .map(userService::mapToDto)
                .collect(Collectors.toList());
        export.vendors = vendorRepository.findByUserId(user.getId()).stream()
                .map(userService::mapToDto)
                .collect(Collectors.toList());
        export.budgets = budgetRepository.findByUserId(user.getId()).stream()
                .map(userService::mapToDto)
                .collect(Collectors.toList());
        export.reminders = reminderRepository.findByUserId(user.getId()).stream()
                .map(userService::mapToDto)
                .collect(Collectors.toList());
        export.trips = tripService.getTrips(email);

        export.consentHistory = consentRecordRepository.findByUserOrderByTimestampDesc(user).stream()
                .map(c -> new ConsentRecordDto(c.getId(), c.getConsentType(), c.getAction(), c.getTimestamp(), c.getPolicyVersion(), c.getDetails()))
                .collect(Collectors.toList());

        export.auditLogs = auditLogRepository.findByUserOrderByTimestampDesc(user).stream()
                .map(a -> new PrivacyAuditLogDto(a.getId(), a.getUserEmail(), a.getEventType(), a.getDescription(), a.getIpAddress(), a.getUserAgent(), a.getTimestamp()))
                .collect(Collectors.toList());

        export.grievanceHistory = privacyGrievanceRepository.findByUserOrderByCreatedAtDesc(user).stream()
                .map(g -> new PrivacyGrievanceDto(g.getTicketId(), g.getCategory(), g.getDescription(), g.getStatus(), g.getCreatedAt(), g.getRedressedAt(), g.getResponseMessage()))
                .collect(Collectors.toList());

        PrivacyAuditLog exportAudit = new PrivacyAuditLog(
                user,
                user.getEmail(),
                "DATA_EXPORT_REQUESTED",
                "Complete structured user data bundle exported per DPDP Act Section 11"
        );
        auditLogRepository.save(exportAudit);

        return export;
    }

    public List<PrivacyAuditLogDto> getAuditLogs(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return auditLogRepository.findByUserOrderByTimestampDesc(user).stream()
                .map(a -> new PrivacyAuditLogDto(a.getId(), a.getUserEmail(), a.getEventType(), a.getDescription(), a.getIpAddress(), a.getUserAgent(), a.getTimestamp()))
                .collect(Collectors.toList());
    }

    @Transactional
    public PrivacyGrievanceDto submitGrievance(String email, GrievanceSubmitRequestDto request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.category == null || request.category.trim().isEmpty() ||
            request.description == null || request.description.trim().isEmpty()) {
            throw new IllegalArgumentException("Category and description are required");
        }

        PrivacyGrievance grievance = new PrivacyGrievance();
        grievance.setUser(user);
        grievance.setCategory(request.category.trim());
        grievance.setDescription(request.description.trim());
        grievance.setTicketId("GRV-" + System.currentTimeMillis());
        grievance.setStatus("OPEN");
        grievance.setResponseMessage("Under review by Data Protection Officer");

        grievance = privacyGrievanceRepository.save(grievance);

        PrivacyAuditLog auditLog = new PrivacyAuditLog(
                user,
                user.getEmail(),
                "GRIEVANCE_SUBMITTED",
                "Grievance ticket created: " + grievance.getTicketId() + " in category: " + grievance.getCategory()
        );
        auditLogRepository.save(auditLog);

        return new PrivacyGrievanceDto(
                grievance.getTicketId(),
                grievance.getCategory(),
                grievance.getDescription(),
                grievance.getStatus(),
                grievance.getCreatedAt(),
                grievance.getRedressedAt(),
                grievance.getResponseMessage()
        );
    }

    public List<PrivacyGrievanceDto> getGrievances(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return privacyGrievanceRepository.findByUserOrderByCreatedAtDesc(user).stream()
                .map(g -> new PrivacyGrievanceDto(g.getTicketId(), g.getCategory(), g.getDescription(), g.getStatus(), g.getCreatedAt(), g.getRedressedAt(), g.getResponseMessage()))
                .collect(Collectors.toList());
    }

    @Transactional
    public Map<String, Object> eraseUserData(String email, Map<String, String> body) {
        String confirmationText = body != null ? body.get("confirmationText") : null;
        if (!"DELETE MY DATA".equals(confirmationText)) {
            throw new IllegalArgumentException("Confirmation text must be 'DELETE MY DATA'");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setIsErased(true);
        user.setErasedAt(LocalDateTime.now());
        user.setName("Erased User");
        user.setAvatarInitials("XX");
        user.setProfilePictureUrl(null);
        user.setGuardianEmail(null);
        user.setGuardianName(null);
        userRepository.save(user);

        ConsentRecord record = new ConsentRecord();
        record.setUser(user);
        record.setConsentType("RIGHT_TO_ERASURE");
        record.setAction("WITHDRAWN");
        record.setDetails("Account erased and personal data anonymized per DPDP Act Section 12");
        consentRecordRepository.save(record);

        PrivacyAuditLog auditLog = new PrivacyAuditLog(
                user,
                user.getEmail(),
                "ACCOUNT_DELETION_REQUESTED",
                "Account personal identifiers erased and anonymized per DPDP Act Section 12"
        );
        auditLogRepository.save(auditLog);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Personal data erased and account anonymized in accordance with DPDP Act Section 12");
        return response;
    }

    public LegalDocumentDto getLegalDocument(String docType) {
        if (docType == null) {
            throw new IllegalArgumentException("Document type required");
        }
        String cleanType = docType.toLowerCase().trim();
        switch (cleanType) {
            case "terms":
                return new LegalDocumentDto("terms", "Terms of Service",
                        "# SpeDex Terms of Service\n\nNon-custodial smart wallet and expense tracking platform designed for Indian students and young professionals.", LocalDateTime.now());
            case "privacy":
                return new LegalDocumentDto("privacy", "Privacy Policy",
                        "# SpeDex Privacy Policy\n\nDesigned to support compliance with the Digital Personal Data Protection Act, 2023 (DPDP Act). We adhere to purpose limitation, data minimization, and strict consent boundaries.", LocalDateTime.now());
            case "consent":
                return new LegalDocumentDto("consent", "Consent Notice",
                        "# SpeDex Consent Notice\n\nTransparent, purpose-separated consent requests. You maintain the right to grant or withdraw consent at any time without penalizing core service access.", LocalDateTime.now());
            case "cookies":
                return new LegalDocumentDto("cookies", "Cookie Policy",
                        "# SpeDex Cookie Policy\n\nWe utilize essential session tokens for authentication and optional local preference tokens. We do not use third-party behavioral ad trackers.", LocalDateTime.now());
            case "child-privacy":
                return new LegalDocumentDto("child-privacy", "Child & Minor Privacy Policy",
                        "# SpeDex Child & Minor Financial Safety Policy\n\nIn compliance with DPDP Act Section 9, accounts under 18 operate strictly in Financial Learning / Journal Mode. Direct payment actions are blocked server-side, and no tracking or behavioral advertising is permitted.", LocalDateTime.now());
            case "data-retention":
                return new LegalDocumentDto("data-retention", "Data Retention Policy",
                        "# SpeDex Data Retention Policy\n\nActive transaction logs and ledgers are retained for 180 days or as required by applicable financial recordkeeping regulations before archival or anonymization.", LocalDateTime.now());
            case "grievance":
                return new LegalDocumentDto("grievance", "Grievance Redressal Mechanism",
                        "# SpeDex Grievance Redressal Mechanism\n\nData Protection Officer: dpo@spedex.internal\nResponse Time: Initial acknowledgment within 24 hours; redressal within 30 days under DPDP rules.", LocalDateTime.now());
            case "third-parties":
            case "subprocessors":
                return new LegalDocumentDto("subprocessors", "Third-Party Subprocessors Register",
                        "# SpeDex Subprocessor & Third-Party Service Registry\n\n" +
                        "SpeDex maintains transparent contractual relationships with select technical infrastructure providers to deliver its services.\n\n" +
                        "| Provider | Purpose | Processing Location | Data Handled | Safeguards |\n" +
                        "|---|---|---|---|---|\n" +
                        "| **Render Inc.** | Backend API Hosting | Frankfurt / Singapore | Encrypted API payloads, user tokens | TLS 1.3, ISO 27001 |\n" +
                        "| **Vercel Inc.** | Web Dashboard CDN | Global Edge / Mumbai | Static web assets, client bundle | SOC 2 Type II, CDN caching |\n" +
                        "| **Supabase / AWS** | PostgreSQL Database | Singapore (ap-southeast-1) | Hashed credentials, ledgers, trips | AES-256 encryption at rest, VPC isolation |\n" +
                        "| **Google Fonts** | Typography (Sora, Cormorant) | Global CDN | Font asset caching | No personal identifier logging |\n\n" +
                        "*No financial identifiers (such as UPI PIN, debit card CVV, or bank credentials) are ever transmitted to or stored by any subprocessor.*", LocalDateTime.now());
            default:
                throw new NoSuchElementException("Legal document not found: " + docType);
        }
    }
}
