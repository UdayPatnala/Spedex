package com.spedex.controller;

import com.spedex.dto.TransactionDto;
import com.spedex.model.Transaction;
import com.spedex.model.User;
import com.spedex.model.Vendor;
import com.spedex.repository.TransactionRepository;
import com.spedex.repository.UserRepository;
import com.spedex.repository.VendorRepository;
import com.spedex.service.UserService;
import com.spedex.repository.TripRepository;
import com.spedex.model.TripStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import com.spedex.model.PrivacyAuditLog;
import com.spedex.repository.PrivacyAuditLogRepository;
import com.spedex.service.UserCapabilityService;
import org.springframework.http.HttpStatus;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private UserCapabilityService capabilityService;

    @Autowired
    private PrivacyAuditLogRepository auditLogRepository;

    @PostMapping("/prepare")
    public ResponseEntity<Map<String, Object>> preparePayment(@RequestBody Map<String, Object> payload) {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!capabilityService.canInitiatePayment(user)) {
            PrivacyAuditLog auditLog = new PrivacyAuditLog(
                    user,
                    user.getEmail(),
                    "PAYMENT_ACTION_BLOCKED",
                    "Payment initiation blocked for minor or restricted account. Payee: " + payload.getOrDefault("payee_name", "Unknown")
            );
            auditLogRepository.save(auditLog);

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "PAYMENT_ACTION_BLOCKED_FOR_MINOR");
            errorResponse.put("message", "Payment initiation and UPI shortcuts are disabled for accounts under 18. Manual journaling and budgeting remain available.");
            errorResponse.put("mode", "LEARNING_JOURNAL");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }

        double amount = payload.get("amount") instanceof Number number ? number.doubleValue() : 0.0;
        String payeeName = String.valueOf(payload.getOrDefault("payee_name", "Unknown Vendor"));
        String upiHandle = String.valueOf(payload.getOrDefault("upi_handle", ""));

        Vendor vendor = null;
        Object vendorId = payload.get("vendor_id");
        if (vendorId instanceof Number number) {
            vendor = vendorRepository.findById(number.longValue()).orElse(null);
        }

        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setVendor(vendor);
        transaction.setDescription("Payment to " + payeeName);
        transaction.setCategory(vendor == null ? "Payments" : vendor.getCategory());
        transaction.setAmount(amount);
        transaction.setDirection("expense");
        transaction.setPaymentMethod("upi");
        transaction.setAccountLabel("Primary UPI");
        transaction.setStatus("pending");
        transaction.setOccurredAt(LocalDateTime.now());

        // Auto-link to active trip if one exists
        tripRepository.findByUserAndStatus(user, TripStatus.ACTIVE)
                .ifPresent(transaction::setTrip);

        transaction = transactionRepository.save(transaction);

        TransactionDto transactionDto = userService.mapToDto(transaction);

        Map<String, Object> response = new HashMap<>();
        response.put("transaction", transactionDto);
        response.put("upi_url", "upi://pay?pa=" + upiHandle + "&pn=" + payeeName + "&am=" + amount + "&cu=INR");
        response.put("redirect_message", "Redirecting to UPI app...");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{transactionId}/complete")
    public ResponseEntity<Map<String, Object>> completePayment(@PathVariable Long transactionId, @RequestBody Map<String, String> payload) {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        if (!transaction.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        String requestedStatus = payload.getOrDefault("status", "failed");
        String normalizedStatus = "completed".equalsIgnoreCase(requestedStatus) || "success".equalsIgnoreCase(requestedStatus)
                ? "success"
                : "failed";
        transaction.setStatus(normalizedStatus);
        transactionRepository.save(transaction);

        Map<String, Object> response = new HashMap<>();
        response.put("transaction_id", transactionId);
        response.put("status", normalizedStatus);
        response.put("message", "Payment state updated");
        return ResponseEntity.ok(response);
    }
}
