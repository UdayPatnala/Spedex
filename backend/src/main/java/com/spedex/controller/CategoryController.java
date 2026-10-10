package com.spedex.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private static final List<Map<String, String>> DEFAULT_CATEGORIES = Arrays.asList(
            createCategory("Food", "restaurant", "accent-rose", "Dining, snacks, chai, and canteen"),
            createCategory("Groceries", "local_mall", "accent-amber", "Daily essentials, vegetables, toiletries"),
            createCategory("Transport", "directions_car", "accent-lavender", "Metro, autos, fuel, bus, and trains"),
            createCategory("Shopping", "shopping_bag", "accent-mint", "Clothing, electronics, online deliveries"),
            createCategory("Entertainment", "movie", "accent-rose", "Movies, outings, games, and streaming"),
            createCategory("Education", "school", "accent-lavender", "Books, stationery, courses, and exam fees"),
            createCategory("Bills", "receipt", "accent-amber", "Electricity, mobile recharge, Wi-Fi, rent"),
            createCategory("Health", "medical_services", "accent-mint", "Pharmacy, doctor consultations, wellness"),
            createCategory("Subscriptions", "loyalty", "accent-lavender", "Software, memberships, recurring plans"),
            createCategory("Personal", "person", "accent-rose", "Self care, haircuts, gifts, leisure"),
            createCategory("Other", "more_horiz", "accent-lavender", "Miscellaneous expenses")
    );

    private static Map<String, String> createCategory(String id, String icon, String accent, String description) {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("id", id);
        map.put("name", id);
        map.put("icon", icon);
        map.put("accent", accent);
        map.put("description", description);
        return map;
    }

    @GetMapping
    public ResponseEntity<List<Map<String, String>>> listCategories() {
        return ResponseEntity.ok(DEFAULT_CATEGORIES);
    }
}
