package com.spedex.controller;

import com.spedex.dto.VendorDto;
import com.spedex.service.PaymentReferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payment-references")
public class PaymentReferenceController {

    @Autowired
    private PaymentReferenceService paymentReferenceService;

    private String getAuthEmail() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @GetMapping
    public ResponseEntity<List<VendorDto>> listReferences() {
        return ResponseEntity.ok(paymentReferenceService.listReferences(getAuthEmail()));
    }

    @PostMapping
    public ResponseEntity<VendorDto> createReference(@RequestBody VendorDto input) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentReferenceService.createReference(getAuthEmail(), input));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendorDto> getReference(@PathVariable Long id) {
        return ResponseEntity.ok(paymentReferenceService.getReference(getAuthEmail(), id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VendorDto> updateReference(@PathVariable Long id, @RequestBody VendorDto input) {
        return ResponseEntity.ok(paymentReferenceService.updateReference(getAuthEmail(), id, input));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteReference(@PathVariable Long id) {
        return ResponseEntity.ok(paymentReferenceService.deleteReference(getAuthEmail(), id));
    }
}
