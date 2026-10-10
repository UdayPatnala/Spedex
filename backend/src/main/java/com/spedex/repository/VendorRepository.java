package com.spedex.repository;

import com.spedex.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface VendorRepository extends JpaRepository<Vendor, Long> {
    List<Vendor> findByUserId(Long userId);
    Optional<Vendor> findByIdAndUserId(Long id, Long userId);
}
