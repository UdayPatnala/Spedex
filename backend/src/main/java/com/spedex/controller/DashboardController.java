package com.spedex.controller;

import com.spedex.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/dashboard/overview")
    public ResponseEntity<Map<String, Object>> overview() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(dashboardService.getOverview(email));
    }

    @GetMapping("/mobile/vendors")
    public ResponseEntity<Map<String, Object>> vendors() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(dashboardService.getVendors(email));
    }

    @GetMapping("/mobile/budgets")
    public ResponseEntity<Map<String, Object>> budgets() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(dashboardService.getBudgets(email));
    }

    @GetMapping("/mobile/analytics")
    public ResponseEntity<Map<String, Object>> analytics() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(dashboardService.getAnalytics(email));
    }
}
