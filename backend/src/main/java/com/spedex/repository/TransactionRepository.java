package com.spedex.repository;

import com.spedex.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {
    List<Transaction> findByUserId(Long userId);
    List<Transaction> findByUserIdOrderByOccurredAtDesc(Long userId);
    Page<Transaction> findByUserId(Long userId, Pageable pageable);
    Optional<Transaction> findByIdAndUserId(Long id, Long userId);
}
