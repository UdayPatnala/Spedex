package com.spedex.repository;

import com.spedex.model.ConsentRecord;
import com.spedex.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsentRecordRepository extends JpaRepository<ConsentRecord, Long> {
    List<ConsentRecord> findByUserOrderByTimestampDesc(User user);
    List<ConsentRecord> findByUser_EmailOrderByTimestampDesc(String email);
}
