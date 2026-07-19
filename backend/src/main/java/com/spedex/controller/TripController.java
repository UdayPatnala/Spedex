package com.spedex.controller;

import com.spedex.dto.TransactionDto;
import com.spedex.dto.TripDetailsDto;
import com.spedex.dto.TripDto;
import com.spedex.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TripController {

    @Autowired
    private TripService tripService;

    @PostMapping("/trips")
    public ResponseEntity<TripDto> startTrip(@RequestBody Map<String, Object> payload) {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Object nameObj = payload.get("name");
        if (!(nameObj instanceof String name) || name.trim().isEmpty()) {
            throw new RuntimeException("Invalid trip name");
        }
        TripDto tripDto = tripService.startTrip(name, email);
        return new ResponseEntity<>(tripDto, HttpStatus.CREATED);
    }

    @GetMapping("/trips")
    public ResponseEntity<List<TripDto>> getTrips() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<TripDto> trips = tripService.getTrips(email);
        return ResponseEntity.ok(trips);
    }

    @GetMapping("/trips/{id}")
    public ResponseEntity<TripDetailsDto> getTripDetails(@PathVariable Long id) {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        TripDetailsDto details = tripService.getTripDetails(id, email);
        return ResponseEntity.ok(details);
    }

    @PostMapping("/trips/{id}/complete")
    public ResponseEntity<TripDto> completeTrip(@PathVariable Long id) {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        TripDto tripDto = tripService.completeTrip(id, email);
        return ResponseEntity.ok(tripDto);
    }

    @PostMapping("/trips/{id}/transactions")
    public ResponseEntity<TransactionDto> addManualTransaction(
            @PathVariable Long id,
            @RequestBody Map<String, Object> payload) {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Object amountObj = payload.get("amount");
        if (!(amountObj instanceof Number number)) {
            throw new RuntimeException("Invalid amount");
        }
        double amount = number.doubleValue();
        if (amount <= 0 || Double.isNaN(amount) || Double.isInfinite(amount)) {
            throw new RuntimeException("Invalid amount");
        }

        Object descObj = payload.get("description");
        if (descObj != null && !(descObj instanceof String)) {
            throw new RuntimeException("Invalid description");
        }
        String description = descObj != null ? (String) descObj : "";

        Object catObj = payload.get("category");
        if (catObj != null && !(catObj instanceof String)) {
            throw new RuntimeException("Invalid category");
        }
        String category = catObj != null ? (String) catObj : "";

        TransactionDto txDto = tripService.addManualTransaction(id, amount, description, category, email);
        return new ResponseEntity<>(txDto, HttpStatus.CREATED);
    }
}
