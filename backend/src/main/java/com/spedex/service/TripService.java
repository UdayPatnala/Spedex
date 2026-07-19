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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class TripService {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    public TripDto startTrip(String name, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        tripRepository.findByUserAndStatus(user, TripStatus.ACTIVE)
                .ifPresent(activeTrip -> {
                    activeTrip.setStatus(TripStatus.COMPLETED);
                    activeTrip.setCompletedAt(LocalDateTime.now());
                    tripRepository.save(activeTrip);
                });

        Trip trip = new Trip();
        trip.setName(name);
        trip.setUser(user);
        trip.setStatus(TripStatus.ACTIVE);
        trip.setCreatedAt(LocalDateTime.now());

        Trip savedTrip = tripRepository.save(trip);
        return mapToDto(savedTrip);
    }

    public TripDto completeTrip(Long tripId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        if (!trip.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        if (trip.getStatus() == TripStatus.ACTIVE) {
            trip.setStatus(TripStatus.COMPLETED);
            trip.setCompletedAt(LocalDateTime.now());
            trip = tripRepository.save(trip);
        }

        return mapToDto(trip);
    }

    public List<TripDto> getTrips(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Trip> trips = tripRepository.findByUser(user);
        return trips.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public TripDetailsDto getTripDetails(Long tripId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        if (!trip.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        // Fetch transactions of the trip. Filter to only completed expenses (where direction equals "expense")
        List<Transaction> expenses = trip.getTransactions().stream()
                .filter(t -> t != null
                        && "completed".equalsIgnoreCase(t.getStatus())
                        && "expense".equalsIgnoreCase(t.getDirection()))
                .collect(Collectors.toList());

        double totalSpend = expenses.stream()
                .mapToDouble(Transaction::getAmount)
                .sum();

        double cashSpend = expenses.stream()
                .filter(t -> t.getPaymentMethod() != null && t.getPaymentMethod().equalsIgnoreCase("CASH"))
                .mapToDouble(Transaction::getAmount)
                .sum();

        double cardOnlineSpend = totalSpend - cashSpend;

        // categoryBreakdown: group expenses by category. Sum amount per category.
        Map<String, Double> categoryAmounts = expenses.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getCategory() == null || t.getCategory().isBlank() ? "Miscellaneous" : t.getCategory(),
                        Collectors.summingDouble(Transaction::getAmount)
                ));

        List<CategoryBreakdownDto> breakdownList = new ArrayList<>();
        for (Map.Entry<String, Double> entry : categoryAmounts.entrySet()) {
            String category = entry.getKey();
            double amount = entry.getValue();
            double percentage = totalSpend == 0.0 ? 0.0 : (amount / totalSpend) * 100.0;
            double roundedPercentage = Math.round(percentage * 10.0) / 10.0;
            breakdownList.add(new CategoryBreakdownDto(category, amount, roundedPercentage));
        }

        // Sort category breakdown list by amount descending
        breakdownList.sort((a, b) -> Double.compare(b.amount, a.amount));

        // Map the trip's transaction list to TransactionDtos using userService.mapToDto, sorting by occurredAt descending
        List<TransactionDto> transactionDtos = trip.getTransactions().stream()
                .sorted((a, b) -> {
                    LocalDateTime timeA = a.getOccurredAt();
                    LocalDateTime timeB = b.getOccurredAt();
                    if (timeA == null && timeB == null) return 0;
                    if (timeA == null) return 1;
                    if (timeB == null) return -1;
                    return timeB.compareTo(timeA); // descending
                })
                .map(userService::mapToDto)
                .collect(Collectors.toList());

        TripDetailsDto details = new TripDetailsDto();
        details.id = trip.getId();
        details.name = trip.getName();
        details.status = trip.getStatus() != null ? trip.getStatus().name() : null;
        details.createdAt = trip.getCreatedAt() == null
                ? null
                : trip.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        details.completedAt = trip.getCompletedAt() == null
                ? null
                : trip.getCompletedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        details.totalSpend = totalSpend;
        details.cashSpend = cashSpend;
        details.cardOnlineSpend = cardOnlineSpend;
        details.categoryBreakdown = breakdownList;
        details.transactions = transactionDtos;

        return details;
    }

    public TransactionDto addManualTransaction(Long tripId, Double amount, String description, String category, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        if (!trip.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        if (trip.getStatus() != TripStatus.ACTIVE) {
            throw new RuntimeException("Cannot add transactions to a completed trip");
        }

        String normalizedCategory = (category == null || category.isBlank()) ? "Miscellaneous" : category;

        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setTrip(trip);
        transaction.setAmount(amount);
        transaction.setDescription(description);
        transaction.setCategory(normalizedCategory);
        transaction.setDirection("expense");
        transaction.setPaymentMethod("CASH");
        transaction.setAccountLabel("Primary Account");
        transaction.setStatus("completed");
        transaction.setOccurredAt(LocalDateTime.now());

        Transaction savedTransaction = transactionRepository.save(transaction);
        return userService.mapToDto(savedTransaction);
    }

    private TripDto mapToDto(Trip trip) {
        if (trip == null) return null;
        TripDto dto = new TripDto();
        dto.id = trip.getId();
        dto.name = trip.getName();
        dto.status = trip.getStatus() != null ? trip.getStatus().name() : null;
        dto.createdAt = trip.getCreatedAt() == null
                ? null
                : trip.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        dto.completedAt = trip.getCompletedAt() == null
                ? null
                : trip.getCompletedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        return dto;
    }
}
