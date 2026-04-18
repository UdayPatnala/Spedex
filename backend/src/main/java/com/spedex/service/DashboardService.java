package com.spedex.service;

import com.spedex.dto.SpedexUserDto;
import com.spedex.model.User;
import com.spedex.model.Vendor;
import com.spedex.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private ReminderRepository reminderRepository;

    @Autowired
    private UserService userService;

    public Map<String, Object> getOverview(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        SpedexUserDto userDto = userService.mapToDto(user);

        Map<String, Object> overview = new HashMap<>();
        overview.put("user", userDto);
        overview.put("monthly_total", 4250.0); // Mocking for now, in a real app query DB
        overview.put("monthly_budget", 15000.0);
        overview.put("budget_used_ratio", 4250.0 / 15000.0);
        overview.put("budget_copy", "You’ve spent 28% of your monthly budget. You're on track!");
        
        List<Vendor> quickPay = vendorRepository.findByUserId(user.getId()).stream()
                .filter(v -> v.getIsQuickPay())
                .limit(3)
                .collect(Collectors.toList());
        overview.put("quick_pay", quickPay);
        
        overview.put("recent_transactions", new ArrayList<>());
        overview.put("reminders", new ArrayList<>());
        overview.put("weekly_spending", Arrays.asList(1200, 1500, 800, 750));
        overview.put("peak_day_label", "Wednesday");
        overview.put("weekly_average", 1062.5);
        overview.put("security_message", "Your wallet is secured with multi-factor UPI authentication.");

        return overview;
    }

    public Map<String, Object> getVendors(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        List<Vendor> vendors = vendorRepository.findByUserId(user.getId());
        
        Map<String, List<Vendor>> groups = vendors.stream()
                .collect(Collectors.groupingBy(Vendor::getCategory));

        Map<String, Object> response = new HashMap<>();
        response.put("user", userService.mapToDto(user));
        response.put("groups", groups);
        return response;
    }

    public Map<String, Object> getBudgets(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        
        Map<String, Object> response = new HashMap<>();
        response.put("remaining_budget", 10750.0);
        response.put("budgets", new ArrayList<>());
        response.put("reminders", new ArrayList<>());
        response.put("savings_tip", "Switching your coffee subscription to a weekly plan could save you ₹400/month.");
        return response;
    }

    public Map<String, Object> getAnalytics(String email) {
        Map<String, Object> response = new HashMap<>();
        response.put("total_spent", 4250.0);
        response.put("smart_insight", "Your spending is 15% lower than last month. Great job!");
        response.put("category_breakdown", new ArrayList<>());
        response.put("weekly_spend", new ArrayList<>());
        response.put("highest_sector", Map.of("title", "Dining", "subtitle", "₹1,800 spent", "accent", "rose", "icon", "restaurant"));
        response.put("busiest_day", Map.of("title", "Wednesday", "subtitle", "4 transactions", "accent", "mint", "icon", "event_busy"));
        response.put("weekday_ratio", 70);
        response.put("weekend_ratio", 30);
        return response;
    }
}
