package com.spedex.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class PaymentController {

    @PostMapping("/prepare")
    public ResponseEntity<Map<String, Object>> preparePayment(@RequestBody Map<String, Object> payload) {
        // Mock implementation
        Map<String, Object> transaction = new HashMap<>();
        transaction.put("id", 12345);
        transaction.put("description", "Payment to " + payload.get("payee_name"));
        transaction.put("amount", payload.get("amount"));
        transaction.put("status", "pending");

        Map<String, Object> response = new HashMap<>();
        response.put("transaction", transaction);
        response.put("upi_url", "upi://pay?pa=" + payload.get("upi_handle") + "&pn=" + payload.get("payee_name") + "&am=" + payload.get("amount"));
        response.put("redirect_message", "Redirecting to UPI app...");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{transactionId}/complete")
    public ResponseEntity<Map<String, Object>> completePayment(@PathVariable Long transactionId, @RequestBody Map<String, String> payload) {
        // Mock implementation
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Payment " + payload.get("status"));
        return ResponseEntity.ok(response);
    }
}
