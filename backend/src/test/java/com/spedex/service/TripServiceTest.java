package com.spedex.service;

import com.spedex.dto.CategoryBreakdownDto;
import com.spedex.dto.TransactionDto;
import com.spedex.dto.TripDetailsDto;
import com.spedex.dto.TripDto;
import com.spedex.model.Transaction;
import com.spedex.model.Trip;
import com.spedex.model.TripStatus;
import com.spedex.model.User;
import com.spedex.repository.TransactionRepository;
import com.spedex.repository.TripRepository;
import com.spedex.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TripServiceTest {

    @Mock
    private TripRepository tripRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private TripService tripService;

    private User mockUser;
    private Trip mockTrip;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setName("John Doe");
        mockUser.setEmail("john@example.com");

        mockTrip = new Trip();
        mockTrip.setId(10L);
        mockTrip.setName("Business Trip");
        mockTrip.setUser(mockUser);
        mockTrip.setStatus(TripStatus.ACTIVE);
        mockTrip.setCreatedAt(LocalDateTime.now().minusDays(1));
    }

    @Test
    void startTrip_Success_NoActiveTrip() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(mockUser));
        when(tripRepository.findByUserAndStatus(mockUser, TripStatus.ACTIVE)).thenReturn(Optional.empty());
        when(tripRepository.save(any(Trip.class))).thenAnswer(invocation -> {
            Trip t = invocation.getArgument(0);
            t.setId(11L);
            return t;
        });

        TripDto dto = tripService.startTrip("New Trip", "john@example.com");

        assertNotNull(dto);
        assertEquals(11L, dto.id);
        assertEquals("New Trip", dto.name);
        assertEquals("ACTIVE", dto.status);
        assertNotNull(dto.createdAt);
        assertNull(dto.completedAt);
    }

    @Test
    void startTrip_Success_WithActiveTrip() {
        Trip activeTrip = new Trip();
        activeTrip.setId(12L);
        activeTrip.setStatus(TripStatus.ACTIVE);
        activeTrip.setUser(mockUser);

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(mockUser));
        when(tripRepository.findByUserAndStatus(mockUser, TripStatus.ACTIVE)).thenReturn(Optional.of(activeTrip));
        when(tripRepository.save(any(Trip.class))).thenAnswer(invocation -> {
            Trip t = invocation.getArgument(0);
            if (t.getId() == null) {
                t.setId(13L);
            }
            return t;
        });

        TripDto dto = tripService.startTrip("New Trip 2", "john@example.com");

        assertEquals(TripStatus.COMPLETED, activeTrip.getStatus());
        assertNotNull(activeTrip.getCompletedAt());
        verify(tripRepository).save(activeTrip);

        assertNotNull(dto);
        assertEquals(13L, dto.id);
        assertEquals("ACTIVE", dto.status);
    }

    @Test
    void startTrip_UserNotFound_ThrowsException() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        Exception ex = assertThrows(RuntimeException.class, () -> tripService.startTrip("Trip", "nonexistent@example.com"));
        assertEquals("User not found", ex.getMessage());
    }

    @Test
    void completeTrip_Success() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(mockUser));
        when(tripRepository.findById(10L)).thenReturn(Optional.of(mockTrip));
        when(tripRepository.save(any(Trip.class))).thenReturn(mockTrip);

        TripDto dto = tripService.completeTrip(10L, "john@example.com");

        assertNotNull(dto);
        assertEquals(TripStatus.COMPLETED, mockTrip.getStatus());
        assertNotNull(mockTrip.getCompletedAt());
        assertEquals("COMPLETED", dto.status);
    }

    @Test
    void completeTrip_TripNotFound_ThrowsException() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(mockUser));
        when(tripRepository.findById(999L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(RuntimeException.class, () -> tripService.completeTrip(999L, "john@example.com"));
        assertEquals("Trip not found", ex.getMessage());
    }

    @Test
    void completeTrip_Unauthorized_ThrowsException() {
        User otherUser = new User();
        otherUser.setId(2L);
        mockTrip.setUser(otherUser);

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(mockUser));
        when(tripRepository.findById(10L)).thenReturn(Optional.of(mockTrip));

        Exception ex = assertThrows(RuntimeException.class, () -> tripService.completeTrip(10L, "john@example.com"));
        assertEquals("Access denied", ex.getMessage());
    }

    @Test
    void getTrips_Success() {
        List<Trip> trips = List.of(mockTrip);
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(mockUser));
        when(tripRepository.findByUser(mockUser)).thenReturn(trips);

        List<TripDto> result = tripService.getTrips("john@example.com");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(10L, result.get(0).id);
    }

    @Test
    void getTripDetails_Success() {
        Transaction expense1 = new Transaction();
        expense1.setId(101L);
        expense1.setAmount(100.0);
        expense1.setCategory("Food");
        expense1.setDirection("expense");
        expense1.setPaymentMethod("CASH");
        expense1.setStatus("completed");
        expense1.setOccurredAt(LocalDateTime.now().minusHours(2));

        Transaction expense2 = new Transaction();
        expense2.setId(102L);
        expense2.setAmount(200.0);
        expense2.setCategory("Transport");
        expense2.setDirection("expense");
        expense2.setPaymentMethod("CARD");
        expense2.setStatus("completed");
        expense2.setOccurredAt(LocalDateTime.now().minusHours(1));

        Transaction ignoredIncome = new Transaction();
        ignoredIncome.setId(103L);
        ignoredIncome.setAmount(500.0);
        ignoredIncome.setCategory("Refund");
        ignoredIncome.setDirection("income");
        ignoredIncome.setStatus("completed");
        ignoredIncome.setOccurredAt(LocalDateTime.now().minusHours(3));

        mockTrip.getTransactions().add(expense1);
        mockTrip.getTransactions().add(expense2);
        mockTrip.getTransactions().add(ignoredIncome);

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(mockUser));
        when(tripRepository.findById(10L)).thenReturn(Optional.of(mockTrip));

        TransactionDto dto1 = new TransactionDto();
        dto1.id = 101L;
        dto1.amount = 100.0;
        dto1.occurredAt = expense1.getOccurredAt().toString();

        TransactionDto dto2 = new TransactionDto();
        dto2.id = 102L;
        dto2.amount = 200.0;
        dto2.occurredAt = expense2.getOccurredAt().toString();

        TransactionDto dto3 = new TransactionDto();
        dto3.id = 103L;
        dto3.amount = 500.0;
        dto3.occurredAt = ignoredIncome.getOccurredAt().toString();

        when(userService.mapToDto(expense1)).thenReturn(dto1);
        when(userService.mapToDto(expense2)).thenReturn(dto2);
        when(userService.mapToDto(ignoredIncome)).thenReturn(dto3);

        TripDetailsDto details = tripService.getTripDetails(10L, "john@example.com");

        assertNotNull(details);
        assertEquals(300.0, details.totalSpend);
        assertEquals(100.0, details.cashSpend);
        assertEquals(200.0, details.cardOnlineSpend);

        assertNotNull(details.categoryBreakdown);
        assertEquals(2, details.categoryBreakdown.size());
        CategoryBreakdownDto breakdown1 = details.categoryBreakdown.get(0);
        assertEquals("Transport", breakdown1.category);
        assertEquals(200.0, breakdown1.amount);
        assertEquals(66.7, breakdown1.percentage);

        CategoryBreakdownDto breakdown2 = details.categoryBreakdown.get(1);
        assertEquals("Food", breakdown2.category);
        assertEquals(100.0, breakdown2.amount);
        assertEquals(33.3, breakdown2.percentage);

        assertNotNull(details.transactions);
        assertEquals(3, details.transactions.size());
        assertEquals(102L, details.transactions.get(0).id);
        assertEquals(101L, details.transactions.get(1).id);
        assertEquals(103L, details.transactions.get(2).id);
    }

    @Test
    void addManualTransaction_Success() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(mockUser));
        when(tripRepository.findById(10L)).thenReturn(Optional.of(mockTrip));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TransactionDto resultDto = new TransactionDto();
        resultDto.id = 201L;
        resultDto.amount = 50.0;
        resultDto.description = "Coffee";
        resultDto.category = "Food";
        resultDto.paymentMethod = "CASH";
        resultDto.status = "completed";

        when(userService.mapToDto(any(Transaction.class))).thenReturn(resultDto);

        TransactionDto dto = tripService.addManualTransaction(10L, 50.0, "Coffee", "Food", "john@example.com");

        assertNotNull(dto);
        assertEquals(50.0, dto.amount);
        assertEquals("Food", dto.category);
        assertEquals("CASH", dto.paymentMethod);
    }

    @Test
    void addManualTransaction_CompletedTrip_ThrowsException() {
        mockTrip.setStatus(TripStatus.COMPLETED);

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(mockUser));
        when(tripRepository.findById(10L)).thenReturn(Optional.of(mockTrip));

        Exception ex = assertThrows(RuntimeException.class, () ->
                tripService.addManualTransaction(10L, 50.0, "Coffee", "Food", "john@example.com"));
        assertEquals("Cannot add transactions to a completed trip", ex.getMessage());
    }
}
