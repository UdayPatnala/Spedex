package com.spedex.controller;

import com.spedex.dto.TransactionDto;
import com.spedex.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    private String getAuthEmail() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> listTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String direction,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Double minAmount,
            @RequestParam(required = false) Double maxAmount
    ) {
        return ResponseEntity.ok(transactionService.listTransactions(
                getAuthEmail(), page, size, search, category, direction, startDate, endDate, minAmount, maxAmount
        ));
    }

    @PostMapping
    public ResponseEntity<TransactionDto> createTransaction(@RequestBody TransactionDto input) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.createTransaction(getAuthEmail(), input));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDto> getTransaction(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getTransaction(getAuthEmail(), id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionDto> updateTransaction(
            @PathVariable Long id,
            @RequestBody TransactionDto input
    ) {
        return ResponseEntity.ok(transactionService.updateTransaction(getAuthEmail(), id, input));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteTransaction(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.deleteTransaction(getAuthEmail(), id));
    }
}
