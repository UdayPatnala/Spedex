package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SpedexRepository(private val db: AppDatabase) {
    val user: Flow<User?> = db.userDao().getUserById()
    val allVendors: Flow<List<Vendor>> = db.vendorDao().getAllVendors()
    val quickPayVendors: Flow<List<Vendor>> = db.vendorDao().getQuickPayVendors()
    val allTransactions: Flow<List<Transaction>> = db.transactionDao().getAllTransactions()
    val allBudgets: Flow<List<Budget>> = db.budgetDao().getAllBudgets()
    val allReminders: Flow<List<Reminder>> = db.reminderDao().getAllReminders()
    val activeTrip: Flow<Trip?> = db.tripDao().getActiveTrip()
    val allTrips: Flow<List<Trip>> = db.tripDao().getAllTrips()
    val privacySettings: Flow<PrivacySettings?> = db.privacyDao().getPrivacySettings()
    val allAuditLogs: Flow<List<PrivacyAuditLogItem>> = db.privacyDao().getAllAuditLogs()

    suspend fun saveUser(user: User) = withContext(Dispatchers.IO) {
        db.userDao().insertOrUpdateUser(user)
    }

    suspend fun insertVendor(vendor: Vendor) = withContext(Dispatchers.IO) {
        db.vendorDao().insertVendor(vendor)
    }

    suspend fun updateVendor(vendor: Vendor) = withContext(Dispatchers.IO) {
        db.vendorDao().updateVendor(vendor)
    }

    suspend fun deleteVendor(vendor: Vendor) = withContext(Dispatchers.IO) {
        db.vendorDao().deleteVendor(vendor)
    }

    suspend fun insertTransaction(transaction: Transaction): Long = withContext(Dispatchers.IO) {
        val result = db.transactionDao().insertTransaction(transaction)
        // If it's a successful transaction, we want to update the budget spent
        if (transaction.status == "SUCCESS") {
            recalculateBudgetSpentForCategory(transaction.category)
        }
        result
    }

    suspend fun updateTransactionStatus(transactionId: Int, status: String) = withContext(Dispatchers.IO) {
        db.transactionDao().updateTransactionStatus(transactionId, status)
        // Since status changed, update corresponding budget category
        // Find transaction first to get category
        val txs = db.transactionDao().getAllTransactions().firstOrNull()
        val tx = txs?.find { it.id == transactionId }
        if (tx != null) {
            recalculateBudgetSpentForCategory(tx.category)
        }
    }

    suspend fun deleteTransaction(transaction: Transaction) = withContext(Dispatchers.IO) {
        db.transactionDao().deleteTransaction(transaction)
        recalculateBudgetSpentForCategory(transaction.category)
    }

    suspend fun insertBudget(budget: Budget) = withContext(Dispatchers.IO) {
        db.budgetDao().insertBudget(budget)
        recalculateBudgetSpentForCategory(budget.category)
    }

    suspend fun updateBudget(budget: Budget) = withContext(Dispatchers.IO) {
        db.budgetDao().updateBudget(budget)
    }

    suspend fun deleteBudget(budget: Budget) = withContext(Dispatchers.IO) {
        db.budgetDao().deleteBudget(budget)
    }

    suspend fun insertReminder(reminder: Reminder) = withContext(Dispatchers.IO) {
        db.reminderDao().insertReminder(reminder)
    }

    suspend fun updateReminder(reminder: Reminder) = withContext(Dispatchers.IO) {
        db.reminderDao().updateReminder(reminder)
    }

    suspend fun deleteReminder(reminder: Reminder) = withContext(Dispatchers.IO) {
        db.reminderDao().deleteReminder(reminder)
    }

    suspend fun getVendorByUpi(upi: String): Vendor? = withContext(Dispatchers.IO) {
        db.vendorDao().getVendorByUpi(upi)
    }

    // Trip management
    suspend fun startTrip(name: String, budget: Double, currency: String = "INR", exchangeRate: Double = 1.0): Long = withContext(Dispatchers.IO) {
        db.tripDao().closeAllActiveTrips()
        val tripId = db.tripDao().insertTrip(
            Trip(
                name = name,
                budget = budget,
                currency = currency,
                exchangeRate = exchangeRate,
                status = "ACTIVE",
                totalSpend = 0.0
            )
        )
        db.privacyDao().insertAuditLog(
            PrivacyAuditLogItem(
                eventType = "TRIP_STARTED",
                details = "Started new travel trip '$name' (Budget: $currency $budget, Rate: $exchangeRate)"
            )
        )
        tripId
    }

    suspend fun completeTrip(tripId: Int) = withContext(Dispatchers.IO) {
        db.tripDao().closeAllActiveTrips()
        db.privacyDao().insertAuditLog(
            PrivacyAuditLogItem(
                eventType = "TRIP_COMPLETED",
                details = "Completed trip ID $tripId and archived expense summary"
            )
        )
    }

    suspend fun addSpendToActiveTrip(amount: Double) = withContext(Dispatchers.IO) {
        val active = db.tripDao().getActiveTrip().firstOrNull()
        if (active != null) {
            db.tripDao().addSpendToTrip(active.id, amount)
        }
    }

    // Privacy & DPDP Management
    suspend fun updatePrivacyConsent(
        essential: Boolean,
        analytics: Boolean,
        marketing: Boolean,
        location: Boolean,
        ai: Boolean
    ) = withContext(Dispatchers.IO) {
        val current = db.privacyDao().getPrivacySettings().firstOrNull() ?: PrivacySettings()
        val updated = current.copy(
            essentialConsent = essential,
            analyticsConsent = analytics,
            marketingConsent = marketing,
            locationConsent = location,
            aiConsent = ai,
            updatedAt = System.currentTimeMillis()
        )
        db.privacyDao().insertOrUpdatePrivacySettings(updated)
        db.privacyDao().insertAuditLog(
            PrivacyAuditLogItem(
                eventType = "CONSENT_UPDATED",
                details = "Updated consent: analytics=$analytics, marketing=$marketing, location=$location, ai=$ai"
            )
        )
    }

    suspend fun requestGuardianConsent(guardianName: String, guardianEmail: String) = withContext(Dispatchers.IO) {
        val current = db.privacyDao().getPrivacySettings().firstOrNull() ?: PrivacySettings(isMinor = true, age = 16)
        val updated = current.copy(
            guardianName = guardianName,
            guardianEmail = guardianEmail,
            guardianConsentStatus = "PENDING",
            updatedAt = System.currentTimeMillis()
        )
        db.privacyDao().insertOrUpdatePrivacySettings(updated)
        db.privacyDao().insertAuditLog(
            PrivacyAuditLogItem(
                eventType = "GUARDIAN_CONSENT_REQUESTED",
                details = "Dispatched verifiable parental consent token to $guardianEmail ($guardianName)"
            )
        )
    }

    suspend fun verifyGuardianConsent() = withContext(Dispatchers.IO) {
        val current = db.privacyDao().getPrivacySettings().firstOrNull() ?: return@withContext
        val updated = current.copy(
            guardianConsentStatus = "VERIFIED",
            updatedAt = System.currentTimeMillis()
        )
        db.privacyDao().insertOrUpdatePrivacySettings(updated)
        db.privacyDao().insertAuditLog(
            PrivacyAuditLogItem(
                eventType = "GUARDIAN_CONSENT_VERIFIED",
                details = "Parental consent verified for minor account"
            )
        )
    }

    suspend fun exportUserDataJson(): String = withContext(Dispatchers.IO) {
        val user = db.userDao().getUserById().firstOrNull()
        val txs = db.transactionDao().getAllTransactions().firstOrNull() ?: emptyList()
        val budgets = db.budgetDao().getAllBudgets().firstOrNull() ?: emptyList()
        val reminders = db.reminderDao().getAllReminders().firstOrNull() ?: emptyList()
        val trips = db.tripDao().getAllTrips().firstOrNull() ?: emptyList()
        val privacy = db.privacyDao().getPrivacySettings().firstOrNull()
        val audit = db.privacyDao().getAllAuditLogs().firstOrNull() ?: emptyList()

        db.privacyDao().insertAuditLog(
            PrivacyAuditLogItem(
                eventType = "DATA_EXPORT_REQUESTED",
                details = "Generated machine-readable personal data export archive (JSON)"
            )
        )

        """
        {
            "export_version": "2.06.00.0",
            "timestamp": ${System.currentTimeMillis()},
            "compliance": "DPDP Act 2023 Section 11",
            "user": {
                "id": "${user?.id ?: ""}",
                "name": "${user?.name ?: ""}",
                "email": "${user?.email ?: ""}",
                "plan": "${user?.plan ?: ""}"
            },
            "privacy": {
                "is_minor": ${privacy?.isMinor ?: false},
                "age": ${privacy?.age ?: 20},
                "essential_consent": ${privacy?.essentialConsent ?: true},
                "analytics_consent": ${privacy?.analyticsConsent ?: false},
                "marketing_consent": ${privacy?.marketingConsent ?: false},
                "location_consent": ${privacy?.locationConsent ?: false},
                "ai_consent": ${privacy?.aiConsent ?: false},
                "guardian_consent_status": "${privacy?.guardianConsentStatus ?: "NOT_REQUIRED"}"
            },
            "transactions_count": ${txs.size},
            "budgets_count": ${budgets.size},
            "reminders_count": ${reminders.size},
            "trips_count": ${trips.size},
            "audit_logs_count": ${audit.size}
        }
        """.trimIndent()
    }

    suspend fun eraseUserData() = withContext(Dispatchers.IO) {
        db.transactionDao().deleteAllTransactions()
        db.budgetDao().deleteAllBudgets()
        db.reminderDao().deleteAllReminders()
        db.tripDao().deleteAllTrips()
        db.userDao().insertOrUpdateUser(
            User(
                id = "user_default",
                name = "Erased User",
                email = "erased@user.local",
                plan = "Anonymized Account",
                avatarInitials = "XX"
            )
        )
        db.privacyDao().insertAuditLog(
            PrivacyAuditLogItem(
                eventType = "ACCOUNT_DELETION_REQUESTED",
                details = "Executed irreversible data erasure and account anonymization per DPDP Act Section 12"
            )
        )
    }

    private suspend fun recalculateBudgetSpentForCategory(category: String) {
        val txList = db.transactionDao().getAllTransactions().firstOrNull() ?: emptyList()
        val successSpent = txList
            .filter { it.category.equals(category, ignoreCase = true) && it.status == "SUCCESS" && it.direction == "OUT" }
            .sumOf { it.amount }
        db.budgetDao().updateBudgetSpent(category, successSpent)
    }

    suspend fun seedInitialDataIfNeeded() = withContext(Dispatchers.IO) {
        val currentUser = db.userDao().getUserById().firstOrNull()
        if (currentUser == null) {
            // Seed User
            db.userDao().insertOrUpdateUser(
                User(
                    id = "user_default",
                    name = "Uday Patnala",
                    email = "udaypatnala5@gmail.com",
                    plan = "Student Starter Pro",
                    avatarInitials = "UP",
                    memberSince = "May 2026"
                )
            )

            // Seed Vendors
            db.vendorDao().insertVendor(Vendor(name = "Campus Canteen", category = "Food", icon = "fastfood", accent = "teal", upiHandle = "campus.canteen@okhdfc", defaultAmount = 45.0, isQuickPay = true))
            db.vendorDao().insertVendor(Vendor(name = "Sai Xerox Shop", category = "Xerox", icon = "print", accent = "indigo", upiHandle = "saixeroxtpt@okaxis", defaultAmount = 15.0, isQuickPay = true))
            db.vendorDao().insertVendor(Vendor(name = "Rickshaw Campus Shuttle", category = "Transport", icon = "directions_car", accent = "rose", upiHandle = "riteshauto@oksbi", defaultAmount = 30.0, isQuickPay = true))
            db.vendorDao().insertVendor(Vendor(name = "Hostel Laundry Express", category = "Subscriptions", icon = "local_laundry_service", accent = "orange", upiHandle = "laundrexxhostel@paytm", defaultAmount = 150.0, isQuickPay = false))
            db.vendorDao().insertVendor(Vendor(name = "Campus Tea Stall", category = "Food", icon = "coffee", accent = "emerald", upiHandle = "campus.chai@okicici", defaultAmount = 12.0, isQuickPay = true))

            // Seed Budgets
            db.budgetDao().insertBudget(Budget(category = "Food", icon = "fastfood", accent = "teal", limitAmount = 3000.0, spent = 1140.0))
            db.budgetDao().insertBudget(Budget(category = "Xerox", icon = "print", accent = "indigo", limitAmount = 600.0, spent = 165.0))
            db.budgetDao().insertBudget(Budget(category = "Transport", icon = "directions_car", accent = "rose", limitAmount = 1000.0, spent = 240.0))
            db.budgetDao().insertBudget(Budget(category = "Subscriptions", icon = "subscriptions", accent = "orange", limitAmount = 1500.0, spent = 350.0))
            db.budgetDao().insertBudget(Budget(category = "Rent", icon = "home", accent = "cyan", limitAmount = 5000.0, spent = 0.0))

            // Seed Reminders
            db.reminderDao().insertReminder(Reminder(title = "Canteen Monthly Mess Bill", subtitle = "Deducts hosteler share automatically", amount = 850.0, dueDate = "Every 5th", autopayEnabled = true))
            db.reminderDao().insertReminder(Reminder(title = "Xerox Quarter Card Renewal", subtitle = "Pre-arranged group Xerox card", amount = 150.0, dueDate = "May 30", autopayEnabled = false))
            db.reminderDao().insertReminder(Reminder(title = "Hostel Gym Shared Pass", subtitle = "Card split with roommates", amount = 300.0, dueDate = "Every 15th", autopayEnabled = false))

            // Seed Transactions
            db.transactionDao().insertTransaction(Transaction(vendorName = "Campus Canteen", vendorUpi = "campus.canteen@okhdfc", description = "Samosa and Lime Soda", category = "Food", amount = 45.0, status = "SUCCESS", occurredAt = System.currentTimeMillis() - 3600000 * 2)) // 2 hrs ago
            db.transactionDao().insertTransaction(Transaction(vendorName = "Sai Xerox Shop", vendorUpi = "saixeroxtpt@okaxis", description = "Notes copying for exams", category = "Xerox", amount = 15.0, status = "SUCCESS", occurredAt = System.currentTimeMillis() - 86400000 * 1)) // 1 day ago
            db.transactionDao().insertTransaction(Transaction(vendorName = "Rickshaw Campus Shuttle", vendorUpi = "riteshauto@oksbi", description = "Campus gate ride", category = "Transport", amount = 30.0, status = "SUCCESS", occurredAt = System.currentTimeMillis() - 86400000 * 2)) // 2 days ago
            db.transactionDao().insertTransaction(Transaction(vendorName = "Hostel Laundry Express", vendorUpi = "laundrexxhostel@paytm", description = "Autopay weekly cycle", category = "Subscriptions", amount = 150.0, status = "SUCCESS", occurredAt = System.currentTimeMillis() - 86400000 * 3)) // 3 days ago
            db.transactionDao().insertTransaction(Transaction(vendorName = "Campus Tea Stall", vendorUpi = "campus.chai@okicici", description = "Chai and Biscuits with group", category = "Food", amount = 25.0, status = "SUCCESS", occurredAt = System.currentTimeMillis() - 86400000 * 4)) // 4 days ago
            db.transactionDao().insertTransaction(Transaction(vendorName = "Netflix Shared Plan", vendorUpi = "netflix@upi", description = "Splitted netflix cost", category = "Subscriptions", amount = 199.0, status = "SUCCESS", occurredAt = System.currentTimeMillis() - 86400000 * 5)) // 5 days ago
            db.transactionDao().insertTransaction(Transaction(vendorName = "Sai Xerox Shop", vendorUpi = "saixeroxtpt@okaxis", description = "Assignment binding", category = "Xerox", amount = 150.0, status = "SUCCESS", occurredAt = System.currentTimeMillis() - 86400000 * 6)) // 6 days ago
            db.transactionDao().insertTransaction(Transaction(vendorName = "Rickshaw Campus Shuttle", vendorUpi = "riteshauto@oksbi", description = "Gate ride returning back", category = "Transport", amount = 210.0, status = "SUCCESS", occurredAt = System.currentTimeMillis() - 86400000 * 7)) // 7 days ago
            db.transactionDao().insertTransaction(Transaction(vendorName = "Campus Canteen", vendorUpi = "campus.canteen@okhdfc", description = "Meal coupon book", category = "Food", amount = 1070.0, status = "SUCCESS", occurredAt = System.currentTimeMillis() - 86400000 * 8)) // 8 days ago
            db.transactionDao().insertTransaction(Transaction(vendorName = "Rickshaw Campus Shuttle", vendorUpi = "riteshauto@oksbi", description = "Station ride", category = "Transport", amount = 120.0, status = "FAILED", occurredAt = System.currentTimeMillis() - 3600000 * 5)) // 5 hrs ago
            db.transactionDao().insertTransaction(Transaction(vendorName = "Hostel Laundry Express", vendorUpi = "laundrexxhostel@paytm", description = "Bedding batch prep", category = "Subscriptions", amount = 200.0, status = "PENDING", occurredAt = System.currentTimeMillis() - 600000)) // 10 mins ago

            // Seed Active Trip
            db.tripDao().insertTrip(
                Trip(
                    id = "trip_demo_1",
                    name = "Goa Hostel Outing",
                    budget = 5000.0,
                    spent = 1450.0,
                    currency = "INR",
                    exchangeRate = 1.0,
                    status = "ACTIVE",
                    createdAt = System.currentTimeMillis() - 86400000 * 2
                )
            )

            // Seed Privacy Settings
            db.privacyDao().insertOrUpdatePrivacySettings(
                PrivacySettings(
                    id = "privacy_default",
                    isMinor = false,
                    age = 20,
                    essentialConsent = true,
                    analyticsConsent = false,
                    marketingConsent = false,
                    locationConsent = false,
                    aiConsent = false,
                    guardianConsentStatus = "NOT_REQUIRED"
                )
            )
            db.privacyDao().insertAuditLog(
                PrivacyAuditLogItem(
                    eventType = "PRIVACY_INITIALIZED",
                    details = "Default DPDP compliance settings loaded (privacy-first baseline)"
                )
            )

            // Re-sync all budgets from transaction values
            recalculateBudgetSpentForCategory("Food")
            recalculateBudgetSpentForCategory("Xerox")
            recalculateBudgetSpentForCategory("Transport")
            recalculateBudgetSpentForCategory("Subscriptions")
            recalculateBudgetSpentForCategory("Rent")
        }
    }

    suspend fun clearAllForDemo() = withContext(Dispatchers.IO) {
        db.transactionDao().deleteAllTransactions()
        db.budgetDao().deleteAllBudgets()
        db.reminderDao().deleteAllReminders()
    }

    suspend fun recalculateAllBudgets() = withContext(Dispatchers.IO) {
        recalculateBudgetSpentForCategory("Food")
        recalculateBudgetSpentForCategory("Xerox")
        recalculateBudgetSpentForCategory("Transport")
        recalculateBudgetSpentForCategory("Subscriptions")
        recalculateBudgetSpentForCategory("Rent")
    }

    suspend fun loadDemoDataset(profileType: String) = withContext(Dispatchers.IO) {
        // 1. Update User to match the profile type
        db.userDao().insertOrUpdateUser(
            when (profileType) {
                "PEASANT" -> User(
                    id = "user_default",
                    name = "Wanderer Al",
                    email = "al.peasant@kingdom.org",
                    plan = "Humble Peasant Tier",
                    avatarInitials = "AL"
                )
                "LORD" -> User(
                    id = "user_default",
                    name = "Lord Uday",
                    email = "uday.lord@estate.com",
                    plan = "Landed Lord Tier",
                    avatarInitials = "LU"
                )
                "EMPEROR" -> User(
                    id = "user_default",
                    name = "Magnificent Emperor",
                    email = "emperor@spedex.empire",
                    plan = "Grand Imperial Tier",
                    avatarInitials = "👑"
                )
                else -> User(
                    id = "user_default",
                    name = "Uday Patnala",
                    email = "udaypatnala5@gmail.com",
                    plan = "Student Starter Pro",
                    avatarInitials = "UP"
                )
            }
        )

        // 2. Clear old data
        db.transactionDao().deleteAllTransactions()
        db.budgetDao().deleteAllBudgets()
        db.reminderDao().deleteAllReminders()

        // 3. Populate custom medieval mock records
        when (profileType) {
            "PEASANT" -> {
                db.transactionDao().insertTransaction(Transaction(vendorName = "Campus Tea Stall", vendorUpi = "campus.chai@okicici", description = "Single modest cup of hot tea", category = "Food", amount = 12.0, status = "SUCCESS"))
                db.transactionDao().insertTransaction(Transaction(vendorName = "Sai Xerox Shop", vendorUpi = "saixeroxtpt@okaxis", description = "One lecture xerox copy", category = "Xerox", amount = 15.0, status = "SUCCESS"))
                db.transactionDao().insertTransaction(Transaction(vendorName = "Rickshaw Campus Shuttle", vendorUpi = "riteshauto@oksbi", description = "One short ride to primary gate", category = "Transport", amount = 18.0, status = "SUCCESS"))

                db.budgetDao().insertBudget(Budget(category = "Food", icon = "fastfood", accent = "teal", limitAmount = 200.0, spent = 12.0))
                db.budgetDao().insertBudget(Budget(category = "Xerox", icon = "print", accent = "indigo", limitAmount = 100.0, spent = 15.0))
                db.budgetDao().insertBudget(Budget(category = "Transport", icon = "directions_car", accent = "rose", limitAmount = 150.0, spent = 18.0))

                db.reminderDao().insertReminder(Reminder(title = "WiFi Contribution Split", subtitle = "Split with Room 202", amount = 100.0, dueDate = "May 30"))
            }
            "LORD" -> {
                db.transactionDao().insertTransaction(Transaction(vendorName = "Campus Canteen", vendorUpi = "campus.canteen@okhdfc", description = "Full dinner feast and juice", category = "Food", amount = 350.0, status = "SUCCESS"))
                db.transactionDao().insertTransaction(Transaction(vendorName = "Sai Xerox Shop", vendorUpi = "saixeroxtpt@okaxis", description = "Midterm exam readings binded", category = "Xerox", amount = 220.0, status = "SUCCESS"))
                db.transactionDao().insertTransaction(Transaction(vendorName = "Hostel Laundry", vendorUpi = "laundry@okaxis", description = "Bi-weekly uniform wash", category = "Subscriptions", amount = 280.0, status = "SUCCESS"))

                db.budgetDao().insertBudget(Budget(category = "Food", icon = "fastfood", accent = "teal", limitAmount = 1500.0, spent = 350.0))
                db.budgetDao().insertBudget(Budget(category = "Xerox", icon = "print", accent = "indigo", limitAmount = 500.0, spent = 220.0))
                db.budgetDao().insertBudget(Budget(category = "Subscriptions", icon = "subscriptions", accent = "orange", limitAmount = 1000.0, spent = 280.0))

                db.reminderDao().insertReminder(Reminder(title = "Weekly Laundry Split", subtitle = "Co-shared with room mates", amount = 140.0, dueDate = "Every Sunday"))
            }
            "EMPEROR" -> {
                db.transactionDao().insertTransaction(Transaction(vendorName = "Imperial Bistro & Dine", vendorUpi = "imperial@okicici", description = "Lavish high-table campus catering", category = "Food", amount = 4200.0, status = "SUCCESS"))
                db.transactionDao().insertTransaction(Transaction(vendorName = "Grand Textbook Hub", vendorUpi = "textbook@okaxis", description = "Whole semester premium course materials", category = "Xerox", amount = 2800.0, status = "SUCCESS"))
                db.transactionDao().insertTransaction(Transaction(vendorName = "Intercity Premium Cab", vendorUpi = "cab@okaxis", description = "Weekend long luxury vacation transit", category = "Transport", amount = 3500.0, status = "SUCCESS"))
                db.transactionDao().insertTransaction(Transaction(vendorName = "Premium Single Flat Room", vendorUpi = "flat@okhdfc", description = "Private balcony flat hostel upgrade", category = "Rent", amount = 2000.0, status = "SUCCESS"))

                db.budgetDao().insertBudget(Budget(category = "Food", icon = "fastfood", accent = "teal", limitAmount = 8000.0, spent = 4200.0))
                db.budgetDao().insertBudget(Budget(category = "Xerox", icon = "print", accent = "indigo", limitAmount = 3500.0, spent = 2800.0))
                db.budgetDao().insertBudget(Budget(category = "Transport", icon = "directions_car", accent = "rose", limitAmount = 5000.0, spent = 3500.0))
                db.budgetDao().insertBudget(Budget(category = "Rent", icon = "home", accent = "cyan", limitAmount = 12000.0, spent = 2000.0))

                db.reminderDao().insertReminder(Reminder(title = "Balcony Rent Premium", subtitle = "High noble standard", amount = 2000.0, dueDate = "1st June", autopayEnabled = true))
            }
            else -> {
                seedInitialDataIfNeeded()
            }
        }

        recalculateAllBudgets()
    }
}
