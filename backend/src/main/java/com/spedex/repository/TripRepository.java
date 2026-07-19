package com.spedex.repository;

import com.spedex.model.Trip;
import com.spedex.model.TripStatus;
import com.spedex.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findByUser(User user);
    Optional<Trip> findByUserAndStatus(User user, TripStatus status);
}
