package com.spedex.repository;

import com.spedex.model.PrivacyGrievance;
import com.spedex.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrivacyGrievanceRepository extends JpaRepository<PrivacyGrievance, Long> {
    List<PrivacyGrievance> findByUserOrderByCreatedAtDesc(User user);
    List<PrivacyGrievance> findByUser_EmailOrderByCreatedAtDesc(String email);
    Optional<PrivacyGrievance> findByTicketId(String ticketId);
}
