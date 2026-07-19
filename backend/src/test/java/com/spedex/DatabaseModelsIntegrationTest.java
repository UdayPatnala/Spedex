package com.spedex;

import com.spedex.model.Trip;
import com.spedex.model.TripStatus;
import com.spedex.model.User;
import com.spedex.model.Transaction;
import com.spedex.repository.TripRepository;
import com.spedex.repository.UserRepository;
import com.spedex.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.annotation.Commit;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DatabaseModelsIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    public void testTripDeletionAndTransactionPreservation() {
        // Create user
        User user = new User();
        user.setName("Test User");
        user.setEmail("test_trip_del_" + System.currentTimeMillis() + "@example.com");
        user.setPasswordHash("pass");
        user = userRepository.save(user);

        // Create Trip
        Trip trip = new Trip();
        trip.setName("Business Trip");
        trip.setUser(user);
        trip.setStatus(TripStatus.ACTIVE);
        trip = tripRepository.save(trip);

        // Create Transaction associated with Trip
        Transaction tx = new Transaction();
        tx.setUser(user);
        tx.setTrip(trip);
        tx.setDescription("Dinner");
        tx.setAmount(100.0);
        tx = transactionRepository.save(tx);

        // Verify association
        assertNotNull(tx.getTrip());
        assertEquals(trip.getId(), tx.getTrip().getId());

        // Add transaction to trip's transaction list to ensure bi-directional consistency
        trip.getTransactions().add(tx);
        trip = tripRepository.save(trip);

        // Now, delete the Trip
        tripRepository.delete(trip);
        tripRepository.flush(); // Force database synchronization

        // Refresh transaction from DB
        Transaction updatedTx = transactionRepository.findById(tx.getId()).orElseThrow();

        // 1. Ensure transaction is preserved (not deleted)
        assertNotNull(updatedTx);
        
        // 2. Ensure trip_id reference becomes NULL
        assertNull(updatedTx.getTrip());
    }

    @Test
    public void testUserDeletionAndTripCascadeDeletion() {
        // Create user
        User user = new User();
        user.setName("Test User Cascade");
        user.setEmail("test_user_del_" + System.currentTimeMillis() + "@example.com");
        user.setPasswordHash("pass");
        user = userRepository.save(user);

        // Create Trip
        Trip trip = new Trip();
        trip.setName("Vacation Trip");
        trip.setUser(user);
        trip.setStatus(TripStatus.ACTIVE);
        trip = tripRepository.save(trip);

        Long tripId = trip.getId();

        // Delete user
        userRepository.delete(user);
        userRepository.flush();

        // Ensure trip is deleted
        Optional<Trip> deletedTrip = tripRepository.findById(tripId);
        assertTrue(deletedTrip.isEmpty());
    }

    @Test
    public void testMultipleActiveTripsConcurrency() {
        // Create user
        User user = new User();
        user.setName("Test User Concurrency");
        user.setEmail("test_concurrency_" + System.currentTimeMillis() + "@example.com");
        user.setPasswordHash("pass");
        user = userRepository.save(user);

        // Create first active trip
        Trip trip1 = new Trip();
        trip1.setName("Trip 1");
        trip1.setUser(user);
        trip1.setStatus(TripStatus.ACTIVE);
        tripRepository.save(trip1);

        // Create second active trip
        Trip trip2 = new Trip();
        trip2.setName("Trip 2");
        trip2.setUser(user);
        trip2.setStatus(TripStatus.ACTIVE);
        
        // Save and flush. If unique constraint exists, this should fail/throw.
        try {
            tripRepository.save(trip2);
            tripRepository.flush();
            System.out.println("SUCCESS_CONCURRENCY: Multiple active trips allowed in DB.");
        } catch (Exception e) {
            System.out.println("FAILURE_CONCURRENCY: Multiple active trips NOT allowed in DB: " + e.getMessage());
            throw e;
        }
    }
}
