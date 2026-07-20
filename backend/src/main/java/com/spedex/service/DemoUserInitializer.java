package com.spedex.service;

import com.spedex.model.*;
import com.spedex.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DemoUserInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final VendorRepository vendorRepository;
    private final BudgetRepository budgetRepository;
    private final ReminderRepository reminderRepository;
    private final TransactionRepository transactionRepository;
    private final TripRepository tripRepository;
    private final PasswordEncoder passwordEncoder;

    public DemoUserInitializer(
            UserRepository userRepository,
            VendorRepository vendorRepository,
            BudgetRepository budgetRepository,
            ReminderRepository reminderRepository,
            TransactionRepository transactionRepository,
            TripRepository tripRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.vendorRepository = vendorRepository;
        this.budgetRepository = budgetRepository;
        this.reminderRepository = reminderRepository;
        this.transactionRepository = transactionRepository;
        this.tripRepository = tripRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        User demoUser = userRepository.findByEmail("demo@gmail.com").orElseGet(User::new);
        demoUser.setName("Demo Account");
        demoUser.setEmail("demo@gmail.com");
        demoUser.setPasswordHash(passwordEncoder.encode("demogorgan"));
        demoUser.setAvatarInitials("DA");
        if (demoUser.getPlan() == null || demoUser.getPlan().isBlank()) {
            demoUser.setPlan("Enterprise Premium");
        }
        User savedUser = userRepository.save(demoUser);

        // Seed Vendors if empty
        if (vendorRepository.findByUserId(savedUser.getId()).isEmpty()) {
            createVendor(savedUser, "Swiggy Food", "Dining", "fastfood", "#EF4444", "swiggy@upi", 380.0, true);
            createVendor(savedUser, "Uber Rides", "Transport", "directions-car", "#3B82F6", "uber@upi", 250.0, true);
            createVendor(savedUser, "DMart Grocery", "Shopping", "shopping-cart", "#10B981", "dmart@upi", 1450.0, true);
            createVendor(savedUser, "Starbucks Coffee", "Dining", "local-cafe", "#8B5CF6", "starbucks@upi", 420.0, false);
        }

        // Seed Budgets if empty
        if (budgetRepository.findByUserId(savedUser.getId()).isEmpty()) {
            createBudget(savedUser, "Food & Dining", "fastfood", "#EF4444", 6850.0, 15000.0);
            createBudget(savedUser, "Transport", "directions-car", "#3B82F6", 3200.0, 8000.0);
            createBudget(savedUser, "Shopping", "shopping-cart", "#10B981", 5400.0, 12000.0);
            createBudget(savedUser, "Utilities", "bolt", "#F59E0B", 4200.0, 10000.0);
        }

        // Seed Reminders if empty
        if (reminderRepository.findByUserId(savedUser.getId()).isEmpty()) {
            createReminder(savedUser, "Apartment Rent", "Monthly lease payment", "rent@upi", 22000.0, LocalDateTime.now().plusDays(5), true, "SCHEDULED");
            createReminder(savedUser, "Electricity Bill", "TATA Power auto-debit", "tatapower@upi", 1850.0, LocalDateTime.now().plusDays(8), true, "SCHEDULED");
            createReminder(savedUser, "High-Speed WiFi", "Airtel Fiber Broadband", "airtel@upi", 999.0, LocalDateTime.now().plusDays(10), false, "SCHEDULED");
        }

        // Seed Trips if empty
        if (tripRepository.findByUser(savedUser).isEmpty()) {
            Trip trip = new Trip();
            trip.setName("Goa Vacation 2026");
            trip.setUser(savedUser);
            trip.setStatus(TripStatus.ACTIVE);
            trip.setCreatedAt(LocalDateTime.now().minusDays(3));
            Trip savedTrip = tripRepository.save(trip);

            createTransaction(savedUser, savedTrip, "Beach Resort Booking", "Lodging", 6500.0, "expense", "CARD", "Primary HDFC Card", "completed");
            createTransaction(savedUser, savedTrip, "Shack Dinner & Drinks", "Dining", 1850.0, "expense", "CASH", "Primary Cash Wallet", "completed");
            createTransaction(savedUser, savedTrip, "Scooter Rental & Fuel", "Transport", 800.0, "expense", "CASH", "Primary Cash Wallet", "completed");
        }
    }

    private void createVendor(User user, String name, String category, String icon, String accent, String upi, double amount, boolean quickPay) {
        Vendor v = new Vendor();
        v.setUser(user);
        v.setName(name);
        v.setCategory(category);
        v.setIcon(icon);
        v.setAccent(accent);
        v.setUpiHandle(upi);
        v.setDefaultAmount(amount);
        v.setIsQuickPay(quickPay);
        vendorRepository.save(v);
    }

    private void createBudget(User user, String category, String icon, String accent, double spent, double limit) {
        Budget b = new Budget();
        b.setUser(user);
        b.setCategory(category);
        b.setIcon(icon);
        b.setAccent(accent);
        b.setSpent(spent);
        b.setLimitAmount(limit);
        budgetRepository.save(b);
    }

    private void createReminder(User user, String title, String subtitle, String upi, double amount, LocalDateTime dueDate, boolean autopay, String status) {
        Reminder r = new Reminder();
        r.setUser(user);
        r.setTitle(title);
        r.setSubtitle(subtitle);
        r.setUpiHandle(upi);
        r.setAmount(amount);
        r.setDueDate(dueDate);
        r.setAutopayEnabled(autopay);
        r.setStatus(status);
        reminderRepository.save(r);
    }

    private void createTransaction(User user, Trip trip, String desc, String category, double amount, String direction, String method, String account, String status) {
        Transaction t = new Transaction();
        t.setUser(user);
        t.setTrip(trip);
        t.setDescription(desc);
        t.setCategory(category);
        t.setAmount(amount);
        t.setDirection(direction);
        t.setPaymentMethod(method);
        t.setAccountLabel(account);
        t.setStatus(status);
        t.setOccurredAt(LocalDateTime.now().minusHours(4));
        transactionRepository.save(t);
    }
}
