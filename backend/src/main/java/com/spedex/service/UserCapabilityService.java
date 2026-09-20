package com.spedex.service;

import com.spedex.dto.UserCapabilitiesDto;
import com.spedex.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserCapabilityService {

    public boolean canInitiatePayment(User user) {
        if (user == null || Boolean.TRUE.equals(user.getIsErased())) {
            return false;
        }
        return !Boolean.TRUE.equals(user.getIsMinor());
    }

    public boolean canOpenPaymentLink(User user) {
        return canInitiatePayment(user);
    }

    public boolean canSavePaymentMethod(User user) {
        return canInitiatePayment(user);
    }

    public boolean canUseMerchantQR(User user) {
        return canInitiatePayment(user);
    }

    public boolean canRecordManualTransaction(User user) {
        return user != null && !Boolean.TRUE.equals(user.getIsErased());
    }

    public boolean canManageBudgets(User user) {
        return user != null && !Boolean.TRUE.equals(user.getIsErased());
    }

    public boolean canUseAnalytics(User user) {
        if (user == null || Boolean.TRUE.equals(user.getIsErased())) {
            return false;
        }
        if (Boolean.TRUE.equals(user.getIsMinor())) {
            return false; // Minors receive strictly offline, non-profiling financial insights
        }
        return Boolean.TRUE.equals(user.getAnalyticsConsent());
    }

    public boolean canReceiveMarketing(User user) {
        if (user == null || Boolean.TRUE.equals(user.getIsErased()) || Boolean.TRUE.equals(user.getIsMinor())) {
            return false;
        }
        return Boolean.TRUE.equals(user.getMarketingConsent());
    }

    public UserCapabilitiesDto getCapabilities(User user) {
        boolean isMinor = user != null && Boolean.TRUE.equals(user.getIsMinor());
        String mode = isMinor ? "LEARNING_JOURNAL" : "FULL_TRANSACTIONAL";
        String notice = isMinor
                ? "Payment shortcuts and live UPI actions are disabled for accounts under 18. Manual expense recording, budgeting, and financial learning are enabled."
                : null;

        return new UserCapabilitiesDto(
                canInitiatePayment(user),
                canOpenPaymentLink(user),
                canSavePaymentMethod(user),
                canUseMerchantQR(user),
                canRecordManualTransaction(user),
                canManageBudgets(user),
                canUseAnalytics(user),
                canReceiveMarketing(user),
                mode,
                notice
        );
    }
}
