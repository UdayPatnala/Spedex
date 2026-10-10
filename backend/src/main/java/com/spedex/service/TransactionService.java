package com.spedex.service;

import com.spedex.dto.TransactionDto;
import com.spedex.model.Transaction;
import com.spedex.model.User;
import com.spedex.model.Vendor;
import com.spedex.repository.TransactionRepository;
import com.spedex.repository.UserRepository;
import com.spedex.repository.VendorRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VendorRepository vendorRepository;

    private User getAuthenticatedUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public Map<String, Object> listTransactions(
            String email,
            int page,
            int size,
            String search,
            String category,
            String direction,
            String startDate,
            String endDate,
            Double minAmount,
            Double maxAmount
    ) {
        User user = getAuthenticatedUser(email);
        Pageable pageable = PageRequest.of(Math.max(0, page), Math.max(1, Math.min(size, 100)), Sort.by("occurredAt").descending());

        Specification<Transaction> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("user").get("id"), user.getId()));

            if (search != null && !search.trim().isEmpty()) {
                String term = "%" + search.trim().toLowerCase() + "%";
                Predicate descMatch = cb.like(cb.lower(root.get("description")), term);
                Predicate catMatch = cb.like(cb.lower(root.get("category")), term);
                predicates.add(cb.or(descMatch, catMatch));
            }

            if (category != null && !category.trim().isEmpty() && !category.equalsIgnoreCase("ALL")) {
                predicates.add(cb.equal(cb.lower(root.get("category")), category.trim().toLowerCase()));
            }

            if (direction != null && !direction.trim().isEmpty() && !direction.equalsIgnoreCase("ALL")) {
                predicates.add(cb.equal(cb.lower(root.get("direction")), direction.trim().toLowerCase()));
            }

            if (startDate != null && !startDate.trim().isEmpty()) {
                try {
                    LocalDateTime start = LocalDateTime.parse(startDate.trim(), DateTimeFormatter.ISO_DATE_TIME);
                    predicates.add(cb.greaterThanOrEqualTo(root.get("occurredAt"), start));
                } catch (Exception ignored) {
                    try {
                        LocalDateTime start = LocalDateTime.parse(startDate.trim() + "T00:00:00");
                        predicates.add(cb.greaterThanOrEqualTo(root.get("occurredAt"), start));
                    } catch (Exception ignored2) {}
                }
            }

            if (endDate != null && !endDate.trim().isEmpty()) {
                try {
                    LocalDateTime end = LocalDateTime.parse(endDate.trim(), DateTimeFormatter.ISO_DATE_TIME);
                    predicates.add(cb.lessThanOrEqualTo(root.get("occurredAt"), end));
                } catch (Exception ignored) {
                    try {
                        LocalDateTime end = LocalDateTime.parse(endDate.trim() + "T23:59:59");
                        predicates.add(cb.lessThanOrEqualTo(root.get("occurredAt"), end));
                    } catch (Exception ignored2) {}
                }
            }

            if (minAmount != null && minAmount >= 0) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("amount"), minAmount));
            }

            if (maxAmount != null && maxAmount >= 0) {
                predicates.add(cb.lessThanOrEqualTo(root.get("amount"), maxAmount));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Transaction> resultPage = transactionRepository.findAll(spec, pageable);
        List<TransactionDto> dtos = resultPage.getContent().stream().map(this::toDto).toList();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("content", dtos);
        response.put("total_elements", resultPage.getTotalElements());
        response.put("total_pages", resultPage.getTotalPages());
        response.put("page_number", resultPage.getNumber());
        response.put("page_size", resultPage.getSize());
        return response;
    }

    @Transactional
    public TransactionDto createTransaction(String email, TransactionDto input) {
        User user = getAuthenticatedUser(email);

        if (input.amount == null || input.amount <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transaction amount must be greater than zero");
        }
        if (input.description == null || input.description.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Description is required");
        }

        Transaction tx = new Transaction();
        tx.setUser(user);
        tx.setAmount(input.amount);
        tx.setDescription(input.description.trim());
        tx.setCategory(input.category != null && !input.category.trim().isEmpty() ? input.category.trim() : "Other");
        tx.setDirection(input.direction != null && input.direction.equalsIgnoreCase("income") ? "income" : "expense");
        tx.setPaymentMethod(input.paymentMethod != null && !input.paymentMethod.trim().isEmpty() ? input.paymentMethod.trim() : "UPI");
        tx.setAccountLabel(input.accountLabel != null && !input.accountLabel.trim().isEmpty() ? input.accountLabel.trim() : "Primary Account");
        tx.setStatus("completed");
        tx.setExternalReference(input.externalReference);

        if (input.vendorId != null) {
            vendorRepository.findByIdAndUserId(input.vendorId, user.getId()).ifPresent(tx::setVendor);
        }

        if (input.occurredAt != null && !input.occurredAt.trim().isEmpty()) {
            try {
                tx.setOccurredAt(LocalDateTime.parse(input.occurredAt.trim(), DateTimeFormatter.ISO_DATE_TIME));
            } catch (Exception e) {
                tx.setOccurredAt(LocalDateTime.now());
            }
        } else {
            tx.setOccurredAt(LocalDateTime.now());
        }

        Transaction saved = transactionRepository.save(tx);
        return toDto(saved);
    }

    public TransactionDto getTransaction(String email, Long id) {
        User user = getAuthenticatedUser(email);
        Transaction tx = transactionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found"));

        if (!tx.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied to transaction");
        }

        return toDto(tx);
    }

    @Transactional
    public TransactionDto updateTransaction(String email, Long id, TransactionDto input) {
        User user = getAuthenticatedUser(email);
        Transaction tx = transactionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found"));

        if (!tx.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied to transaction");
        }

        if (input.amount != null) {
            if (input.amount <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transaction amount must be greater than zero");
            }
            tx.setAmount(input.amount);
        }

        if (input.description != null && !input.description.trim().isEmpty()) {
            tx.setDescription(input.description.trim());
        }

        if (input.category != null && !input.category.trim().isEmpty()) {
            tx.setCategory(input.category.trim());
        }

        if (input.direction != null) {
            tx.setDirection(input.direction.equalsIgnoreCase("income") ? "income" : "expense");
        }

        if (input.paymentMethod != null && !input.paymentMethod.trim().isEmpty()) {
            tx.setPaymentMethod(input.paymentMethod.trim());
        }

        if (input.occurredAt != null && !input.occurredAt.trim().isEmpty()) {
            try {
                tx.setOccurredAt(LocalDateTime.parse(input.occurredAt.trim(), DateTimeFormatter.ISO_DATE_TIME));
            } catch (Exception ignored) {}
        }

        if (input.vendorId != null) {
            vendorRepository.findByIdAndUserId(input.vendorId, user.getId()).ifPresent(tx::setVendor);
        }

        Transaction updated = transactionRepository.save(tx);
        return toDto(updated);
    }

    @Transactional
    public Map<String, Object> deleteTransaction(String email, Long id) {
        User user = getAuthenticatedUser(email);
        Transaction tx = transactionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found"));

        if (!tx.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied to transaction");
        }

        transactionRepository.delete(tx);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "success");
        response.put("message", "Transaction deleted");
        response.put("deleted_id", id);
        return response;
    }

    public String exportTransactionsCsv(String email) {
        User user = getAuthenticatedUser(email);
        List<Transaction> transactions = transactionRepository.findByUserIdOrderByOccurredAtDesc(user.getId());

        StringBuilder sb = new StringBuilder();
        sb.append("ID,Date,Description,Category,Type,Amount,Payment Method,Status,Vendor,Trip ID\n");

        for (Transaction t : transactions) {
            sb.append(t.getId()).append(",");
            sb.append(escapeCsv(t.getOccurredAt() != null ? t.getOccurredAt().toString() : "")).append(",");
            sb.append(escapeCsv(t.getDescription())).append(",");
            sb.append(escapeCsv(t.getCategory())).append(",");
            sb.append(escapeCsv(t.getDirection())).append(",");
            sb.append(t.getAmount() != null ? String.format(Locale.US, "%.2f", t.getAmount()) : "0.00").append(",");
            sb.append(escapeCsv(t.getPaymentMethod())).append(",");
            sb.append(escapeCsv(t.getStatus())).append(",");
            sb.append(escapeCsv(t.getVendor() != null ? t.getVendor().getName() : "")).append(",");
            sb.append(t.getTrip() != null ? t.getTrip().getId() : "").append("\n");
        }

        return sb.toString();
    }

    private String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n") || value.contains("\r")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    private TransactionDto toDto(Transaction tx) {
        TransactionDto dto = new TransactionDto();
        dto.id = tx.getId();
        dto.description = tx.getDescription();
        dto.category = tx.getCategory();
        dto.amount = tx.getAmount();
        dto.direction = tx.getDirection();
        dto.paymentMethod = tx.getPaymentMethod();
        dto.accountLabel = tx.getAccountLabel();
        dto.status = tx.getStatus();
        dto.externalReference = tx.getExternalReference();
        dto.occurredAt = tx.getOccurredAt() != null ? tx.getOccurredAt().toString() : null;
        dto.vendorName = tx.getVendor() != null ? tx.getVendor().getName() : null;
        dto.vendorId = tx.getVendor() != null ? tx.getVendor().getId() : null;
        dto.tripId = tx.getTrip() != null ? tx.getTrip().getId() : null;
        return dto;
    }
}
