package com.spedex.service;

import com.spedex.dto.UserCapabilitiesDto;
import com.spedex.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserCapabilityServiceTest {

    private UserCapabilityService capabilityService;
    private User adultUser;
    private User minorUser;
    private User erasedUser;

    @BeforeEach
    void setUp() {
        capabilityService = new UserCapabilityService();

        adultUser = new User();
        adultUser.setId(1L);
        adultUser.setIsMinor(false);
        adultUser.setAge(21);
        adultUser.setIsErased(false);
        adultUser.setAnalyticsConsent(true);
        adultUser.setMarketingConsent(true);

        minorUser = new User();
        minorUser.setId(2L);
        minorUser.setIsMinor(true);
        minorUser.setAge(16);
        minorUser.setIsErased(false);
        minorUser.setAnalyticsConsent(false);
        minorUser.setMarketingConsent(false);

        erasedUser = new User();
        erasedUser.setId(3L);
        erasedUser.setIsMinor(false);
        erasedUser.setAge(30);
        erasedUser.setIsErased(true);
    }

    @Test
    void adultUser_HasFullTransactionalCapabilities() {
        UserCapabilitiesDto caps = capabilityService.getCapabilities(adultUser);

        assertTrue(caps.canInitiatePayment);
        assertTrue(caps.canOpenPaymentLink);
        assertTrue(caps.canSavePaymentMethod);
        assertTrue(caps.canUseMerchantQR);
        assertTrue(caps.canRecordManualTransaction);
        assertTrue(caps.canManageBudgets);
        assertTrue(caps.canUseAnalytics);
        assertTrue(caps.canReceiveMarketing);
        assertEquals("FULL_TRANSACTIONAL", caps.mode);
        assertNull(caps.restrictionNotice);
    }

    @Test
    void minorUser_RestrictedToLearningJournalMode() {
        UserCapabilitiesDto caps = capabilityService.getCapabilities(minorUser);

        assertFalse(caps.canInitiatePayment, "Minor cannot initiate payments");
        assertFalse(caps.canOpenPaymentLink, "Minor cannot open payment links");
        assertFalse(caps.canSavePaymentMethod, "Minor cannot save payment methods");
        assertFalse(caps.canUseMerchantQR, "Minor cannot use merchant QR codes");
        assertTrue(caps.canRecordManualTransaction, "Minor can record manual transactions");
        assertTrue(caps.canManageBudgets, "Minor can manage budgets");
        assertFalse(caps.canUseAnalytics, "Minor is shielded from profiling analytics");
        assertFalse(caps.canReceiveMarketing, "Minor cannot receive marketing");
        assertEquals("LEARNING_JOURNAL", caps.mode);
        assertNotNull(caps.restrictionNotice);
        assertTrue(caps.restrictionNotice.contains("disabled for accounts under 18"));
    }

    @Test
    void erasedUser_AllCapabilitiesRevoked() {
        UserCapabilitiesDto caps = capabilityService.getCapabilities(erasedUser);

        assertFalse(caps.canInitiatePayment);
        assertFalse(caps.canOpenPaymentLink);
        assertFalse(caps.canSavePaymentMethod);
        assertFalse(caps.canUseMerchantQR);
        assertFalse(caps.canRecordManualTransaction);
        assertFalse(caps.canManageBudgets);
        assertFalse(caps.canUseAnalytics);
        assertFalse(caps.canReceiveMarketing);
    }
}
