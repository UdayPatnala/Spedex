package com.spedex.dto;

public class UserCapabilitiesDto {
    public boolean canInitiatePayment;
    public boolean canOpenPaymentLink;
    public boolean canSavePaymentMethod;
    public boolean canUseMerchantQR;
    public boolean canRecordManualTransaction;
    public boolean canManageBudgets;
    public boolean canUseAnalytics;
    public boolean canReceiveMarketing;
    public String mode; // "FULL_TRANSACTIONAL" vs "LEARNING_JOURNAL"
    public String restrictionNotice;

    public UserCapabilitiesDto() {}

    public UserCapabilitiesDto(boolean canInitiatePayment,
                               boolean canOpenPaymentLink,
                               boolean canSavePaymentMethod,
                               boolean canUseMerchantQR,
                               boolean canRecordManualTransaction,
                               boolean canManageBudgets,
                               boolean canUseAnalytics,
                               boolean canReceiveMarketing,
                               String mode,
                               String restrictionNotice) {
        this.canInitiatePayment = canInitiatePayment;
        this.canOpenPaymentLink = canOpenPaymentLink;
        this.canSavePaymentMethod = canSavePaymentMethod;
        this.canUseMerchantQR = canUseMerchantQR;
        this.canRecordManualTransaction = canRecordManualTransaction;
        this.canManageBudgets = canManageBudgets;
        this.canUseAnalytics = canUseAnalytics;
        this.canReceiveMarketing = canReceiveMarketing;
        this.mode = mode;
        this.restrictionNotice = restrictionNotice;
    }
}
