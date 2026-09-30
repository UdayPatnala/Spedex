package com.example

import com.example.data.PrivacyAuditLogItem
import com.example.data.PrivacySettings
import com.example.data.Trip
import org.junit.Assert.*
import org.junit.Test

class NativeParityUnitTest {

    @Test
    fun testTripForexConversionMath() {
        val tripUsd = Trip(
            id = "trip_test_usd",
            name = "Silicon Valley Tech Tour",
            budget = 83500.0, // 83,500 INR
            spent = 16700.0,  // 16,700 INR
            currency = "USD",
            exchangeRate = 83.50,
            status = "ACTIVE"
        )

        // Base currency math
        val remainingInr = tripUsd.budget - tripUsd.spent
        assertEquals(66800.0, remainingInr, 0.001)

        // Forex foreign currency calculation
        val foreignBudget = tripUsd.budget / tripUsd.exchangeRate
        val foreignSpent = tripUsd.spent / tripUsd.exchangeRate
        assertEquals(1000.0, foreignBudget, 0.001) // 1000 USD
        assertEquals(200.0, foreignSpent, 0.001)   // 200 USD
    }

    @Test
    fun testPrivacySettingsDpdpDefaults() {
        val defaultPrivacy = PrivacySettings(
            id = "privacy_default",
            isMinor = false,
            age = 20,
            essentialConsent = true,
            analyticsConsent = false,
            marketingConsent = false,
            locationConsent = false,
            aiConsent = false,
            guardianConsentStatus = "NOT_REQUIRED"
        )

        assertTrue(defaultPrivacy.essentialConsent)
        assertFalse(defaultPrivacy.analyticsConsent)
        assertFalse(defaultPrivacy.marketingConsent)
        assertFalse(defaultPrivacy.locationConsent)
        assertFalse(defaultPrivacy.aiConsent)
        assertEquals("NOT_REQUIRED", defaultPrivacy.guardianConsentStatus)
    }

    @Test
    fun testMinorProtectionAndGuardianConsentWorkflow() {
        val minorSettings = PrivacySettings(
            id = "privacy_minor",
            isMinor = true,
            age = 16,
            essentialConsent = true,
            analyticsConsent = false,
            marketingConsent = false,
            guardianName = "Paternal Guardian",
            guardianEmail = "guardian@example.com",
            guardianConsentStatus = "PENDING"
        )

        assertTrue(minorSettings.isMinor)
        assertEquals(16, minorSettings.age)
        assertEquals("PENDING", minorSettings.guardianConsentStatus)

        // Simulate verification
        val verifiedSettings = minorSettings.copy(
            guardianConsentStatus = "VERIFIED",
            updatedAt = System.currentTimeMillis()
        )
        assertEquals("VERIFIED", verifiedSettings.guardianConsentStatus)
    }

    @Test
    fun testPrivacyAuditLogCreation() {
        val auditLog = PrivacyAuditLogItem(
            eventType = "CONSENT_UPDATED",
            details = "Updated consent: analytics=true, marketing=false"
        )

        assertEquals("CONSENT_UPDATED", auditLog.eventType)
        assertTrue(auditLog.details.contains("analytics=true"))
        assertTrue(auditLog.timestamp > 0)
    }
}
