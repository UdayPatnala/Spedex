package com.spedex;

import com.spedex.dto.TransactionDto;
import com.spedex.dto.VendorDto;
import com.spedex.model.User;
import com.spedex.repository.UserRepository;
import com.spedex.service.PaymentReferenceService;
import com.spedex.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TransactionControllerIntegrationTest {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private PaymentReferenceService paymentReferenceService;

    @Autowired
    private UserRepository userRepository;

    private User userA;
    private User userB;

    @BeforeEach
    public void setup() {
        long ts = System.currentTimeMillis();
        userA = new User();
        userA.setName("User Alpha");
        userA.setEmail("alpha_" + ts + "@example.com");
        userA.setPasswordHash("hash123");
        userA = userRepository.save(userA);

        userB = new User();
        userB.setName("User Beta");
        userB.setEmail("beta_" + ts + "@example.com");
        userB.setPasswordHash("hash123");
        userB = userRepository.save(userB);
    }

    @Test
    public void testCreateTransactionSuccess() {
        TransactionDto dto = new TransactionDto();
        dto.amount = 250.0;
        dto.description = "College Cafeteria Lunch";
        dto.category = "Food";
        dto.direction = "expense";
        dto.paymentMethod = "UPI";

        TransactionDto created = transactionService.createTransaction(userA.getEmail(), dto);
        assertNotNull(created.id);
        assertEquals(250.0, created.amount);
        assertEquals("College Cafeteria Lunch", created.description);
        assertEquals("Food", created.category);
    }

    @Test
    public void testCreateTransactionValidationFailsOnZeroOrNegativeAmount() {
        TransactionDto dto = new TransactionDto();
        dto.amount = -50.0;
        dto.description = "Invalid negative amount";

        assertThrows(ResponseStatusException.class, () -> {
            transactionService.createTransaction(userA.getEmail(), dto);
        });

        dto.amount = 0.0;
        assertThrows(ResponseStatusException.class, () -> {
            transactionService.createTransaction(userA.getEmail(), dto);
        });
    }

    @Test
    public void testCreateTransactionValidationFailsOnEmptyDescription() {
        TransactionDto dto = new TransactionDto();
        dto.amount = 100.0;
        dto.description = "   ";

        assertThrows(ResponseStatusException.class, () -> {
            transactionService.createTransaction(userA.getEmail(), dto);
        });
    }

    @Test
    public void testUserIsolationAndIdorProtection() {
        TransactionDto dto = new TransactionDto();
        dto.amount = 500.0;
        dto.description = "Alpha Secret Expense";
        dto.category = "Personal";
        TransactionDto createdA = transactionService.createTransaction(userA.getEmail(), dto);

        // User B attempts to read User A's transaction
        assertThrows(ResponseStatusException.class, () -> {
            transactionService.getTransaction(userB.getEmail(), createdA.id);
        });

        // User B attempts to edit User A's transaction
        TransactionDto editDto = new TransactionDto();
        editDto.description = "Hacked by Beta";
        assertThrows(ResponseStatusException.class, () -> {
            transactionService.updateTransaction(userB.getEmail(), createdA.id, editDto);
        });

        // User B attempts to delete User A's transaction
        assertThrows(ResponseStatusException.class, () -> {
            transactionService.deleteTransaction(userB.getEmail(), createdA.id);
        });
    }

    @Test
    public void testUpdateAndDeleteTransaction() {
        TransactionDto dto = new TransactionDto();
        dto.amount = 120.0;
        dto.description = "Auto Rickshaw";
        dto.category = "Transport";
        TransactionDto created = transactionService.createTransaction(userA.getEmail(), dto);

        TransactionDto updateInput = new TransactionDto();
        updateInput.amount = 150.0;
        updateInput.description = "Auto Rickshaw with Tip";
        TransactionDto updated = transactionService.updateTransaction(userA.getEmail(), created.id, updateInput);

        assertEquals(150.0, updated.amount);
        assertEquals("Auto Rickshaw with Tip", updated.description);

        Map<String, Object> delRes = transactionService.deleteTransaction(userA.getEmail(), created.id);
        assertEquals("success", delRes.get("status"));

        assertThrows(ResponseStatusException.class, () -> {
            transactionService.getTransaction(userA.getEmail(), created.id);
        });
    }

    @Test
    public void testListTransactionsWithFilters() {
        TransactionDto t1 = new TransactionDto();
        t1.amount = 100.0;
        t1.description = "Coffee";
        t1.category = "Food";
        transactionService.createTransaction(userA.getEmail(), t1);

        TransactionDto t2 = new TransactionDto();
        t2.amount = 500.0;
        t2.description = "Course Textbook";
        t2.category = "Education";
        transactionService.createTransaction(userA.getEmail(), t2);

        Map<String, Object> listAll = transactionService.listTransactions(
                userA.getEmail(), 0, 10, null, null, null, null, null, null, null
        );
        assertTrue(((List<?>) listAll.get("content")).size() >= 2);

        Map<String, Object> listFood = transactionService.listTransactions(
                userA.getEmail(), 0, 10, null, "Food", null, null, null, null, null
        );
        assertEquals(1, ((List<?>) listFood.get("content")).size());

        Map<String, Object> searchBook = transactionService.listTransactions(
                userA.getEmail(), 0, 10, "Textbook", null, null, null, null, null, null
        );
        assertEquals(1, ((List<?>) searchBook.get("content")).size());
    }

    @Test
    public void testExportCsv() {
        TransactionDto t1 = new TransactionDto();
        t1.amount = 45.0;
        t1.description = "Campus Chai, Biscuit";
        t1.category = "Food";
        transactionService.createTransaction(userA.getEmail(), t1);

        String csv = transactionService.exportTransactionsCsv(userA.getEmail());
        assertNotNull(csv);
        assertTrue(csv.contains("ID,Date,Description,Category,Type,Amount,Payment Method,Status,Vendor,Trip ID"));
        assertTrue(csv.contains("\"Campus Chai, Biscuit\""));
        assertTrue(csv.contains("45.00"));
    }

    @Test
    public void testPaymentReferenceCrudAndUrlValidation() {
        VendorDto ref = new VendorDto();
        ref.name = "Campus Stationery";
        ref.category = "Education";
        ref.upiHandle = "stationery@upi";
        ref.paymentUrl = "https://pay.stationery.example.com";
        ref.notes = "Prints 2 INR per page";

        VendorDto created = paymentReferenceService.createReference(userA.getEmail(), ref);
        assertNotNull(created.id);
        assertEquals("Campus Stationery", created.name);
        assertEquals("https://pay.stationery.example.com", created.paymentUrl);

        // Invalid URL scheme must be rejected
        VendorDto invalidRef = new VendorDto();
        invalidRef.name = "Bad Link";
        invalidRef.paymentUrl = "javascript:alert(1)";
        assertThrows(ResponseStatusException.class, () -> {
            paymentReferenceService.createReference(userA.getEmail(), invalidRef);
        });

        // User B isolation check
        assertThrows(ResponseStatusException.class, () -> {
            paymentReferenceService.getReference(userB.getEmail(), created.id);
        });
    }
}
