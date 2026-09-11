package com.spedex.service;

import com.spedex.dto.*;
import com.spedex.model.ConsentRecord;
import com.spedex.model.PrivacyGrievance;
import com.spedex.model.User;
import com.spedex.repository.ConsentRecordRepository;
import com.spedex.repository.PrivacyGrievanceRepository;
import com.spedex.repository.TripRepository;
import com.spedex.repository.UserRepository;
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
    private TripRepository tripRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TripService tripService;

    // Simulated in-memory storage for parental verification tokens (maps token -> userEmail)
    private final Map<String, String> guardianTokens = new HashMap<>();

    public PrivacySettingsDto getPrivacySettings(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new PrivacySettingsDto(
                user.getAnalyticsConsent(),
                user.getMarketingConsent(),
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

        if (user.getIsMinor()) {
            user.setAnalyticsConsent(false);
            user.setMarketingConsent(false);
        } else {
            if (request.analyticsConsent != null) {
                user.setAnalyticsConsent(request.analyticsConsent);
            }
            if (request.marketingConsent != null) {
                user.setMarketingConsent(request.marketingConsent);
            }
        }

        userRepository.save(user);

        ConsentRecord record = new ConsentRecord();
        record.setUser(user);
        record.setConsentType("PREFERENCES_UPDATE");
        record.setAction("UPDATED");
        record.setDetails("Analytics: " + user.getAnalyticsConsent() + ", Marketing: " + user.getMarketingConsent());
        consentRecordRepository.save(record);

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

        guardianTokens.remove(token);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Guardian consent verified successfully");
        return response;
    }

    public UserDataExportDto exportUserData(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDataExportDto export = new UserDataExportDto();
        export.userData = userService.mapToDto(user);
        export.privacySettings = getPrivacySettings(email);
        export.trips = tripService.getTrips(email);
        export.consentHistory = consentRecordRepository.findByUserOrderByTimestampDesc(user).stream()
                .map(c -> new ConsentRecordDto(c.getId(), c.getConsentType(), c.getAction(), c.getTimestamp(), c.getPolicyVersion(), c.getDetails()))
                .collect(Collectors.toList());
        export.grievanceHistory = privacyGrievanceRepository.findByUserOrderByCreatedAtDesc(user).stream()
                .map(g -> new PrivacyGrievanceDto(g.getTicketId(), g.getCategory(), g.getDescription(), g.getStatus(), g.getCreatedAt(), g.getRedressedAt(), g.getResponseMessage()))
                .collect(Collectors.toList());

        return export;
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
                return new LegalDocumentDto("terms", "Terms of Service", "# SpeDex Terms of Service\n\nNon-custodial smart wallet...", LocalDateTime.now());
            case "privacy":
                return new LegalDocumentDto("privacy", "Privacy Policy", "# SpeDex Privacy Policy\n\nDPDP Act 2023 compliant...", LocalDateTime.now());
            case "consent":
                return new LegalDocumentDto("consent", "Consent Notice", "# SpeDex Consent Notice...", LocalDateTime.now());
            case "cookies":
                return new LegalDocumentDto("cookies", "Cookie Policy", "# SpeDex Cookie Policy...", LocalDateTime.now());
            case "child-privacy":
                return new LegalDocumentDto("child-privacy", "Child & Minor Privacy Policy", "# SpeDex Child Privacy...", LocalDateTime.now());
            case "data-retention":
                return new LegalDocumentDto("data-retention", "Data Retention Policy", "# SpeDex Data Retention Policy...", LocalDateTime.now());
            case "grievance":
                return new LegalDocumentDto("grievance", "Grievance Redressal Mechanism", "# SpeDex Grievance Redressal...", LocalDateTime.now());
            case "third-parties":
                return new LegalDocumentDto("third-parties", "Third-Party Subprocessors Register", "# SpeDex Subprocessors...", LocalDateTime.now());
            default:
                throw new NoSuchElementException("Legal document not found: " + docType);
        }
    }
}
