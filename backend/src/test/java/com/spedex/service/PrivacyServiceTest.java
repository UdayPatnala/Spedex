package com.spedex.service;

import com.spedex.dto.*;
import com.spedex.model.ConsentRecord;
import com.spedex.model.PrivacyAuditLog;
import com.spedex.model.PrivacyGrievance;
import com.spedex.model.User;
import com.spedex.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PrivacyServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ConsentRecordRepository consentRecordRepository;

    @Mock
    private PrivacyGrievanceRepository privacyGrievanceRepository;

    @Mock
    private PrivacyAuditLogRepository auditLogRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private VendorRepository vendorRepository;

    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private ReminderRepository reminderRepository;

    @Mock
    private TripRepository tripRepository;

    @Mock
    private UserService userService;

    @Mock
    private TripService tripService;

    @Mock
    private UserCapabilityService capabilityService;

    @InjectMocks
    private PrivacyService privacyService;

    private User adultUser;
    private User minorUser;

    @BeforeEach
    void setUp() {
        adultUser = new User();
        adultUser.setId(1L);
        adultUser.setName("Adult Tester");
        adultUser.setEmail("adult@example.com");
        adultUser.setIsMinor(false);
        adultUser.setAge(25);
        adultUser.setAnalyticsConsent(false);
        adultUser.setMarketingConsent(false);
        adultUser.setLocationConsent(false);
        adultUser.setAiConsent(false);
        adultUser.setGuardianConsentStatus("NOT_REQUIRED");

        minorUser = new User();
        minorUser.setId(2L);
        minorUser.setName("Minor Tester");
        minorUser.setEmail("minor@example.com");
        minorUser.setIsMinor(true);
        minorUser.setAge(16);
        minorUser.setGuardianEmail("guardian@example.com");
        minorUser.setGuardianName("Guardian Name");
        minorUser.setGuardianConsentStatus("PENDING");
        minorUser.setAnalyticsConsent(false);
        minorUser.setMarketingConsent(false);
        minorUser.setLocationConsent(false);
        minorUser.setAiConsent(false);
    }

    @Test
    void getPrivacySettings_AdultUser_ReturnsSettings() {
        when(userRepository.findByEmail("adult@example.com")).thenReturn(Optional.of(adultUser));

        PrivacySettingsDto settings = privacyService.getPrivacySettings("adult@example.com");

        assertNotNull(settings);
        assertFalse(settings.isMinor);
        assertEquals(25, settings.age);
        assertEquals("NOT_REQUIRED", settings.guardianConsentStatus);
    }

    @Test
    void getPrivacySettings_MinorUser_ReturnsSettings() {
        when(userRepository.findByEmail("minor@example.com")).thenReturn(Optional.of(minorUser));

        PrivacySettingsDto settings = privacyService.getPrivacySettings("minor@example.com");

        assertNotNull(settings);
        assertTrue(settings.isMinor);
        assertEquals(16, settings.age);
        assertEquals("PENDING", settings.guardianConsentStatus);
        assertEquals("guardian@example.com", settings.guardianEmail);
    }

    @Test
    void updateConsents_AdultUser_UpdatesPreferences() {
        when(userRepository.findByEmail("adult@example.com")).thenReturn(Optional.of(adultUser));
        when(userRepository.save(any(User.class))).thenReturn(adultUser);

        ConsentUpdateRequestDto request = new ConsentUpdateRequestDto();
        request.analyticsConsent = true;
        request.marketingConsent = false;
        request.locationConsent = true;
        request.aiConsent = false;

        PrivacySettingsDto updated = privacyService.updateConsents("adult@example.com", request);

        assertTrue(updated.analyticsConsent);
        assertFalse(updated.marketingConsent);
        assertTrue(updated.locationConsent);
        assertFalse(updated.aiConsent);
        verify(consentRecordRepository, times(1)).save(any(ConsentRecord.class));
        verify(auditLogRepository, times(1)).save(any(PrivacyAuditLog.class));
    }

    @Test
    void updateConsents_MinorUser_ForcesDisabledConsents() {
        when(userRepository.findByEmail("minor@example.com")).thenReturn(Optional.of(minorUser));
        when(userRepository.save(any(User.class))).thenReturn(minorUser);

        ConsentUpdateRequestDto request = new ConsentUpdateRequestDto();
        request.analyticsConsent = true;
        request.marketingConsent = true;
        request.locationConsent = true;
        request.aiConsent = true;

        PrivacySettingsDto updated = privacyService.updateConsents("minor@example.com", request);

        assertFalse(updated.analyticsConsent);
        assertFalse(updated.marketingConsent);
        assertFalse(updated.locationConsent);
        assertFalse(updated.aiConsent);
        verify(consentRecordRepository, times(1)).save(any(ConsentRecord.class));
        verify(auditLogRepository, times(1)).save(any(PrivacyAuditLog.class));
    }

    @Test
    void withdrawConsent_ValidType_RevokesConsent() {
        adultUser.setAnalyticsConsent(true);
        when(userRepository.findByEmail("adult@example.com")).thenReturn(Optional.of(adultUser));
        when(userRepository.save(any(User.class))).thenReturn(adultUser);

        PrivacySettingsDto updated = privacyService.withdrawConsent("adult@example.com", "ANALYTICS");

        assertFalse(updated.analyticsConsent);
        verify(consentRecordRepository, times(1)).save(any(ConsentRecord.class));
        verify(auditLogRepository, times(1)).save(any(PrivacyAuditLog.class));
    }

    @Test
    void requestGuardianConsent_ValidInput_GeneratesVerificationToken() {
        when(userRepository.findByEmail("minor@example.com")).thenReturn(Optional.of(minorUser));

        GuardianConsentRequestDto request = new GuardianConsentRequestDto();
        request.guardianName = "Parent Tester";
        request.guardianEmail = "parent@example.com";

        Map<String, Object> res = privacyService.requestGuardianConsent("minor@example.com", request);

        assertTrue((Boolean) res.get("success"));
        assertNotNull(res.get("verificationToken"));
        assertEquals("parent@example.com", res.get("guardianEmail"));
        verify(auditLogRepository, times(1)).save(any(PrivacyAuditLog.class));
    }

    @Test
    void requestGuardianConsent_EmptyInput_ThrowsException() {
        when(userRepository.findByEmail("minor@example.com")).thenReturn(Optional.of(minorUser));

        GuardianConsentRequestDto request = new GuardianConsentRequestDto();
        request.guardianName = "";
        request.guardianEmail = "";

        assertThrows(IllegalArgumentException.class, () ->
            privacyService.requestGuardianConsent("minor@example.com", request)
        );
    }

    @Test
    void verifyGuardianConsent_ValidToken_UpdatesStatusToVerified() {
        when(userRepository.findByEmail("minor@example.com")).thenReturn(Optional.of(minorUser));

        GuardianConsentRequestDto req = new GuardianConsentRequestDto();
        req.guardianName = "Parent";
        req.guardianEmail = "parent@example.com";
        Map<String, Object> reqRes = privacyService.requestGuardianConsent("minor@example.com", req);
        String token = (String) reqRes.get("verificationToken");

        Map<String, Object> verifyRes = privacyService.verifyGuardianConsent(token);

        assertTrue((Boolean) verifyRes.get("success"));
        assertEquals("VERIFIED", minorUser.getGuardianConsentStatus());
        verify(consentRecordRepository, times(1)).save(any(ConsentRecord.class));
        verify(auditLogRepository, atLeastOnce()).save(any(PrivacyAuditLog.class));
    }

    @Test
    void verifyGuardianConsent_InvalidToken_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
            privacyService.verifyGuardianConsent("invalid-token-123")
        );
    }

    @Test
    void exportUserData_ReturnsComprehensiveExport() {
        when(userRepository.findByEmail("adult@example.com")).thenReturn(Optional.of(adultUser));
        when(userService.mapToDto(adultUser)).thenReturn(new SpedexUserDto());
        when(capabilityService.getCapabilities(adultUser)).thenReturn(new UserCapabilitiesDto(true, true, true, true, true, true, true, false, "FULL_TRANSACTIONAL", null));
        when(transactionRepository.findByUserId(adultUser.getId())).thenReturn(new ArrayList<>());
        when(vendorRepository.findByUserId(adultUser.getId())).thenReturn(new ArrayList<>());
        when(budgetRepository.findByUserId(adultUser.getId())).thenReturn(new ArrayList<>());
        when(reminderRepository.findByUserId(adultUser.getId())).thenReturn(new ArrayList<>());
        when(tripService.getTrips("adult@example.com")).thenReturn(new ArrayList<>());
        when(consentRecordRepository.findByUserOrderByTimestampDesc(adultUser)).thenReturn(new ArrayList<>());
        when(auditLogRepository.findByUserOrderByTimestampDesc(adultUser)).thenReturn(new ArrayList<>());
        when(privacyGrievanceRepository.findByUserOrderByCreatedAtDesc(adultUser)).thenReturn(new ArrayList<>());

        UserDataExportDto export = privacyService.exportUserData("adult@example.com");

        assertNotNull(export);
        assertNotNull(export.userData);
        assertNotNull(export.privacySettings);
        assertNotNull(export.capabilities);
        assertNotNull(export.transactions);
        assertNotNull(export.vendors);
        assertNotNull(export.budgets);
        assertNotNull(export.reminders);
        assertNotNull(export.trips);
        assertNotNull(export.consentHistory);
        assertNotNull(export.auditLogs);
        assertNotNull(export.grievanceHistory);
        verify(auditLogRepository, times(1)).save(any(PrivacyAuditLog.class));
    }

    @Test
    void submitGrievance_ValidInput_CreatesGrievanceTicket() {
        when(userRepository.findByEmail("adult@example.com")).thenReturn(Optional.of(adultUser));
        when(privacyGrievanceRepository.save(any(PrivacyGrievance.class))).thenAnswer(invocation -> invocation.getArgument(0));

        GrievanceSubmitRequestDto request = new GrievanceSubmitRequestDto();
        request.category = "CONSENT_REVOCATION";
        request.description = "Revoke my data analytics";

        PrivacyGrievanceDto result = privacyService.submitGrievance("adult@example.com", request);

        assertNotNull(result);
        assertTrue(result.ticketId.startsWith("GRV-"));
        assertEquals("CONSENT_REVOCATION", result.category);
        assertEquals("OPEN", result.status);
        verify(auditLogRepository, times(1)).save(any(PrivacyAuditLog.class));
    }

    @Test
    void eraseUserData_ValidConfirmation_AnonymizesUser() {
        when(userRepository.findByEmail("adult@example.com")).thenReturn(Optional.of(adultUser));

        Map<String, String> body = Map.of("confirmationText", "DELETE MY DATA");
        Map<String, Object> result = privacyService.eraseUserData("adult@example.com", body);

        assertTrue((Boolean) result.get("success"));
        assertTrue(adultUser.getIsErased());
        assertEquals("Erased User", adultUser.getName());
        verify(consentRecordRepository, times(1)).save(any(ConsentRecord.class));
        verify(auditLogRepository, times(1)).save(any(PrivacyAuditLog.class));
    }

    @Test
    void eraseUserData_InvalidConfirmation_ThrowsException() {
        Map<String, String> body = Map.of("confirmationText", "wrong text");

        assertThrows(IllegalArgumentException.class, () ->
            privacyService.eraseUserData("adult@example.com", body)
        );
    }
}
