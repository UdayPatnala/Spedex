package com.spedex.controller;

import com.spedex.dto.TransactionDto;
import com.spedex.model.PrivacyAuditLog;
import com.spedex.model.Transaction;
import com.spedex.model.User;
import com.spedex.repository.PrivacyAuditLogRepository;
import com.spedex.repository.TransactionRepository;
import com.spedex.repository.TripRepository;
import com.spedex.repository.UserRepository;
import com.spedex.repository.VendorRepository;
import com.spedex.service.UserCapabilityService;
import com.spedex.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private VendorRepository vendorRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserService userService;

    @Mock
    private TripRepository tripRepository;

    @Mock
    private UserCapabilityService capabilityService;

    @Mock
    private PrivacyAuditLogRepository auditLogRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private PaymentController paymentController;

    private User adultUser;
    private User minorUser;

    @BeforeEach
    void setUp() {
        adultUser = new User();
        adultUser.setId(1L);
        adultUser.setEmail("adult@example.com");
        adultUser.setIsMinor(false);

        minorUser = new User();
        minorUser.setId(2L);
        minorUser.setEmail("minor@example.com");
        minorUser.setIsMinor(true);

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void preparePayment_BlockedForMinor_Returns403Forbidden() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn("minor@example.com");
        when(userRepository.findByEmail("minor@example.com")).thenReturn(Optional.of(minorUser));
        when(capabilityService.canInitiatePayment(minorUser)).thenReturn(false);

        Map<String, Object> payload = new HashMap<>();
        payload.put("amount", 250.0);
        payload.put("payee_name", "College Cafeteria");

        ResponseEntity<Map<String, Object>> response = paymentController.preparePayment(payload);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("PAYMENT_ACTION_BLOCKED_FOR_MINOR", response.getBody().get("error"));
        assertEquals("LEARNING_JOURNAL", response.getBody().get("mode"));
        verify(auditLogRepository, times(1)).save(any(PrivacyAuditLog.class));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void preparePayment_AllowedForAdult_ReturnsPaymentUrl() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn("adult@example.com");
        when(userRepository.findByEmail("adult@example.com")).thenReturn(Optional.of(adultUser));
        when(capabilityService.canInitiatePayment(adultUser)).thenReturn(true);
        when(tripRepository.findByUserAndStatus(any(), any())).thenReturn(Optional.empty());

        Transaction savedTx = new Transaction();
        savedTx.setId(100L);
        savedTx.setAmount(150.0);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(savedTx);

        TransactionDto txDto = new TransactionDto();
        txDto.id = 100L;
        txDto.amount = 150.0;
        when(userService.mapToDto(any(Transaction.class))).thenReturn(txDto);

        Map<String, Object> payload = new HashMap<>();
        payload.put("amount", 150.0);
        payload.put("payee_name", "Campus Bookstore");
        payload.put("upi_handle", "bookstore@okhdfcbank");

        ResponseEntity<Map<String, Object>> response = paymentController.preparePayment(payload);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("upi_url"));
        assertTrue(response.getBody().get("upi_url").toString().startsWith("upi://pay"));
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }
}
