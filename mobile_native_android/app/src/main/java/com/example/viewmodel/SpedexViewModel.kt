package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.net.URLDecoder
import java.text.SimpleDateFormat
import java.util.*

data class UpiInfo(
    val upiId: String,
    val name: String,
    val amount: Double
)

class SpedexViewModel(private val repository: SpedexRepository) : ViewModel() {

    init {
        viewModelScope.launch {
            repository.seedInitialDataIfNeeded()
        }
    }

    // Reactive states
    val user: StateFlow<User?> = repository.user
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allVendors: StateFlow<List<Vendor>> = repository.allVendors
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val quickPayVendors: StateFlow<List<Vendor>> = repository.quickPayVendors
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allTransactions: StateFlow<List<Transaction>> = repository.allTransactions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allBudgets: StateFlow<List<Budget>> = repository.allBudgets
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allReminders: StateFlow<List<Reminder>> = repository.allReminders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Currently scanned or inputs for transactions
    private val _scannedUpiState = MutableStateFlow<UpiInfo?>(null)
    val scannedUpiState: StateFlow<UpiInfo?> = _scannedUpiState.asStateFlow()

    private val _activeTransactionStatus = MutableStateFlow<String?>(null) // "PREPARE", "PENDING", "SUCCESS", "FAILED"
    val activeTransactionStatus: StateFlow<String?> = _activeTransactionStatus.asStateFlow()

    private val _activeTransactionId = MutableStateFlow<Int?>(null)
    val activeTransactionId: StateFlow<Int?> = _activeTransactionId.asStateFlow()

    fun clearScannedState() {
        _scannedUpiState.value = null
        _activeTransactionStatus.value = null
        _activeTransactionId.value = null
    }

    fun parseUpiString(input: String): UpiInfo? {
        if (!input.contains("upi://pay", ignoreCase = true) && !input.contains("@")) {
            return null
        }
        val targetInput = if (!input.contains("upi://pay")) {
            "upi://pay?pa=$input&pn=CustomMerchant"
        } else {
            input
        }

        return try {
            val decoded = URLDecoder.decode(targetInput, "UTF-8")
            val pa = extractParam(decoded, "pa") ?: ""
            val pnRaw = extractParam(decoded, "pn") ?: "Fast Pay Merchant"
            val pn = if (pnRaw.isBlank()) "Fast Pay Merchant" else pnRaw
            val amStr = extractParam(decoded, "am") ?: "0.0"
            val am = amStr.toDoubleOrNull() ?: 0.0
            
            val info = UpiInfo(upiId = pa, name = pn, amount = am)
            _scannedUpiState.value = info
            info
        } catch (e: Exception) {
            null
        }
    }

    private fun extractParam(uri: String, param: String): String? {
        val pattern = "[?&]$param=([^&]*)"
        val regex = pattern.toRegex()
        val match = regex.find(uri)
        return match?.groups?.get(1)?.value
    }

    // Set an custom manual or scanned upi target explicitly
    fun setScannedUpi(upiInfo: UpiInfo) {
        _scannedUpiState.value = upiInfo
    }

    // Transaction lifecycle actions
    fun preparePaymentAndSaveVendor(
        name: String,
        upi: String,
        amount: Double,
        category: String,
        isQuickPay: Boolean,
        onSuccess: (transactionId: Int, deepLink: String) -> Unit
    ) {
        viewModelScope.launch {
            // 1. Check if vendor exists or create / update it
            val existing = repository.getVendorByUpi(upi)
            if (existing == null) {
                repository.insertVendor(
                    Vendor(
                        name = name,
                        category = category,
                        upiHandle = upi,
                        defaultAmount = amount,
                        isQuickPay = isQuickPay
                    )
                )
            } else if (existing.name != name || existing.category != category || existing.isQuickPay != isQuickPay) {
                repository.updateVendor(
                    existing.copy(
                        name = name,
                        category = category,
                        defaultAmount = amount,
                        isQuickPay = isQuickPay
                    )
                )
            }

            // 2. Prep PENDING transaction
            val transaction = Transaction(
                vendorName = name,
                vendorUpi = upi,
                description = "Quick Spend on $category",
                category = category,
                amount = amount,
                status = "PENDING",
                occurredAt = System.currentTimeMillis()
            )
            val txId = repository.insertTransaction(transaction).toInt()
            _activeTransactionId.value = txId
            _activeTransactionStatus.value = "PENDING"

            // Construct UPI Deep Link
            val encodedName = java.net.URLEncoder.encode(name, "UTF-8")
            val deepLink = "upi://pay?pa=$upi&pn=$encodedName&am=$amount&cu=INR"
            onSuccess(txId, deepLink)
        }
    }

    fun completeActiveTransaction(status: String) {
        val txId = _activeTransactionId.value ?: return
        viewModelScope.launch {
            repository.updateTransactionStatus(txId, status)
            _activeTransactionStatus.value = status
        }
    }

    // CRUD - Profiles
    fun updateProfile(name: String, email: String, initials: String) {
        viewModelScope.launch {
            val curr = user.value ?: User(name = name, email = email, avatarInitials = initials)
            repository.saveUser(
                curr.copy(
                    name = name,
                    email = email,
                    avatarInitials = initials
                )
            )
        }
    }

    fun loadDemoProfile(profileType: String) {
        loadDemoScenario(profileType)
    }

    // CRUD - Budgets
    fun addBudget(category: String, limitAmount: Double, accent: String = "teal") {
        viewModelScope.launch {
            repository.insertBudget(
                Budget(
                    category = category,
                    icon = when (category.lowercase()) {
                        "food" -> "fastfood"
                        "xerox" -> "print"
                        "transport" -> "directions_car"
                        "subscriptions" -> "subscriptions"
                        "rent" -> "home"
                        else -> "monetization_on"
                    },
                    accent = accent,
                    limitAmount = limitAmount
                )
            )
        }
    }

    fun updateBudgetLimit(id: Int, limitAmount: Double) {
        viewModelScope.launch {
            val current = allBudgets.value.find { it.id == id } ?: return@launch
            repository.updateBudget(current.copy(limitAmount = limitAmount))
        }
    }

    fun deleteBudget(budget: Budget) {
        viewModelScope.launch {
            repository.deleteBudget(budget)
        }
    }

    // CRUD - Reminders
    fun addReminder(title: String, subtitle: String, amount: Double, dueDate: String, autopay: Boolean) {
        viewModelScope.launch {
            repository.insertReminder(
                Reminder(
                    title = title,
                    subtitle = subtitle,
                    amount = amount,
                    dueDate = dueDate,
                    autopayEnabled = autopay
                )
            )
        }
    }

    fun updateReminderStatus(id: Int, status: String) {
        viewModelScope.launch {
            val current = allReminders.value.find { it.id == id } ?: return@launch
            repository.updateReminder(current.copy(status = status))
        }
    }

    fun deleteReminder(reminder: Reminder) {
        viewModelScope.launch {
            repository.deleteReminder(reminder)
        }
    }

    // CRUD - Vendors
    fun deleteVendor(vendor: Vendor) {
        viewModelScope.launch {
            repository.deleteVendor(vendor)
        }
    }

    // Dyn Analytics Aggregation (Calculated on state updates)
    val totalExpense: Flow<Double> = repository.allTransactions.map { txList ->
        txList.filter { it.status == "SUCCESS" && it.direction == "OUT" }.sumOf { it.amount }
    }

    val spendByCategory: Flow<Map<String, Double>> = repository.allTransactions.map { txList ->
        txList.filter { it.status == "SUCCESS" && it.direction == "OUT" }
            .groupBy { it.category }
            .mapValues { (_, txs) -> txs.sumOf { it.amount } }
    }

    val weeklyExpenseData: Flow<List<Pair<String, Float>>> = repository.allTransactions.map { txList ->
        val successTx = txList.filter { it.status == "SUCCESS" && it.direction == "OUT" }
        val days = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
        val calendar = Calendar.getInstance()
        val totals = FloatArray(7)

        // Accumulate last 7 days relative to today
        val df = SimpleDateFormat("EEE", Locale.US)
        val daySums = mutableMapOf<String, Float>()
        for (tx in successTx) {
            calendar.timeInMillis = tx.occurredAt
            val dayName = df.format(calendar.time)
            daySums[dayName] = (daySums[dayName] ?: 0f) + tx.amount.toFloat()
        }

        // Return sorted list representation starting from Sunday
        days.map { it to (daySums[it] ?: 0f) }
    }

    val analyticsInsights: Flow<AnalyticsInsights> = repository.allTransactions.map { txList ->
        val successTx = txList.filter { it.status == "SUCCESS" && it.direction == "OUT" }
        if (successTx.isEmpty()) {
            return@map AnalyticsInsights("None", "No data", "0%", 1f)
        }

        // Sector breakdown
        val sectors = successTx.groupBy { it.category }.mapValues { it.value.sumOf { it.amount } }
        val highestSector = sectors.maxByOrNull { it.value }?.key ?: "None"

        // Busiest Day
        val df = SimpleDateFormat("EEEE", Locale.US)
        val cal = Calendar.getInstance()
        val dayCounts = successTx.groupBy {
            cal.timeInMillis = it.occurredAt
            df.format(cal.time)
        }.mapValues { it.value.size }
        val busiestDay = dayCounts.maxByOrNull { it.value }?.key ?: "No transactions yet"

        // Weekday/Weekend Ratio
        var weekdaySpend = 0.0
        var weekendSpend = 0.0
        for (tx in successTx) {
            cal.timeInMillis = tx.occurredAt
            val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
            if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
                weekendSpend += tx.amount
            } else {
                weekdaySpend += tx.amount
            }
        }
        val total = weekdaySpend + weekendSpend
        val weekendRatioPct = if (total > 0) "${((weekendSpend / total) * 100).toInt()}%" else "0%"
        val weekdayRatio = if (total > 0) (weekdaySpend / total).toFloat() else 1f

        AnalyticsInsights(highestSector, busiestDay, weekendRatioPct, weekdayRatio)
    }

    // Medieval Rank State
    val medievalRank: Flow<MedievalRank> = totalExpense.map { spent ->
        MedievalRankHelper.getRankForExpense(spent)
    }

    // Demo Scenarios Loader
    fun loadDemoScenario(scenarioName: String) {
        viewModelScope.launch {
            repository.clearAllForDemo()
            
            when (scenarioName.lowercase()) {
                "peasant" -> {
                    repository.saveUser(
                        User(
                            id = "user_default",
                            name = "Amit Lal",
                            email = "amit.demo@spedex.in",
                            plan = "Frugal Apprentice",
                            avatarInitials = "AL",
                            memberSince = "May 2026"
                        )
                    )
                    // Spent: ₹80.0
                    repository.insertTransaction(Transaction(vendorName = "Campus Tea Stall", vendorUpi = "campus.chai@okicici", description = "Evening Ginger Tea", category = "Food", amount = 15.0, status = "SUCCESS"))
                    repository.insertTransaction(Transaction(vendorName = "Sai Xerox Shop", vendorUpi = "saixeroxtpt@okaxis", description = "Single Page Printout", category = "Xerox", amount = 5.0, status = "SUCCESS"))
                    repository.insertTransaction(Transaction(vendorName = "Rickshaw Campus Shuttle", vendorUpi = "riteshauto@oksbi", description = "Hostel Shuttle", category = "Transport", amount = 60.0, status = "SUCCESS"))
                    
                    repository.insertBudget(Budget(category = "Food", icon = "fastfood", accent = "teal", limitAmount = 1500.0, spent = 15.0))
                    repository.insertBudget(Budget(category = "Xerox", icon = "print", accent = "indigo", limitAmount = 200.0, spent = 5.0))
                    repository.insertBudget(Budget(category = "Transport", icon = "directions_car", accent = "rose", limitAmount = 500.0, spent = 60.0))
                }
                "merchant" -> {
                    repository.saveUser(
                        User(
                            id = "user_default",
                            name = "Amit Lal",
                            email = "amit.demo@spedex.in",
                            plan = "Trade Merchant",
                            avatarInitials = "AL",
                            memberSince = "May 2026"
                        )
                    )
                    // Spent: ₹245.0
                    repository.insertTransaction(Transaction(vendorName = "Campus Tea Stall", vendorUpi = "campus.chai@okicici", description = "Chai & Samosa bundle", category = "Food", amount = 65.0, status = "SUCCESS"))
                    repository.insertTransaction(Transaction(vendorName = "Sai Xerox Shop", vendorUpi = "saixeroxtpt@okaxis", description = "Lab Manual Printing", category = "Xerox", amount = 120.0, status = "SUCCESS"))
                    repository.insertTransaction(Transaction(vendorName = "Rickshaw Campus Shuttle", vendorUpi = "riteshauto@oksbi", description = "Campus gate helper ride", category = "Transport", amount = 60.0, status = "SUCCESS"))
                    
                    repository.insertBudget(Budget(category = "Food", icon = "fastfood", accent = "teal", limitAmount = 1500.0, spent = 65.0))
                    repository.insertBudget(Budget(category = "Xerox", icon = "print", accent = "indigo", limitAmount = 500.0, spent = 120.0))
                    repository.insertBudget(Budget(category = "Transport", icon = "directions_car", accent = "rose", limitAmount = 500.0, spent = 60.0))
                }
                "chief" -> {
                    repository.saveUser(
                        User(
                            id = "user_default",
                            name = "Amit Lal",
                            email = "amit.demo@spedex.in",
                            plan = "Rural Leader (Chief)",
                            avatarInitials = "AL",
                            memberSince = "May 2026"
                        )
                    )
                    // Spent: ₹740.0
                    repository.insertTransaction(Transaction(vendorName = "Campus Canteen", vendorUpi = "campus.canteen@okhdfc", description = "Group snack share", category = "Food", amount = 450.0, status = "SUCCESS"))
                    repository.insertTransaction(Transaction(vendorName = "Sai Xerox Shop", vendorUpi = "saixeroxtpt@okaxis", description = "Record Notebook bind", category = "Xerox", amount = 140.0, status = "SUCCESS"))
                    repository.insertTransaction(Transaction(vendorName = "Hostel Laundry Express", vendorUpi = "laundrexxhostel@paytm", description = "Weekly laundry load", category = "Subscriptions", amount = 150.0, status = "SUCCESS"))
                    
                    repository.insertBudget(Budget(category = "Food", icon = "fastfood", accent = "teal", limitAmount = 3000.0, spent = 450.0))
                    repository.insertBudget(Budget(category = "Xerox", icon = "print", accent = "indigo", limitAmount = 600.0, spent = 140.0))
                    repository.insertBudget(Budget(category = "Subscriptions", icon = "subscriptions", accent = "orange", limitAmount = 1000.0, spent = 150.0))
                }
                "lord" -> {
                    repository.saveUser(
                        User(
                            id = "user_default",
                            name = "Lord Uday",
                            email = "uday.lord@estate.com",
                            plan = "Dormitory Lord",
                            avatarInitials = "LU",
                            memberSince = "May 2026"
                        )
                    )
                    // Spent: ₹1,820.0
                    repository.insertTransaction(Transaction(vendorName = "Campus Canteen", vendorUpi = "campus.canteen@okhdfc", description = "Monthly meal coupons", category = "Food", amount = 1200.0, status = "SUCCESS"))
                    repository.insertTransaction(Transaction(vendorName = "Sai Xerox Shop", vendorUpi = "saixeroxtpt@okaxis", description = "Syllabus Xerox booklet", category = "Xerox", amount = 320.0, status = "SUCCESS"))
                    repository.insertTransaction(Transaction(vendorName = "Hostel Laundry Express", vendorUpi = "laundrexxhostel@paytm", description = "Dorm wash cluster", category = "Subscriptions", amount = 300.0, status = "SUCCESS"))
                    
                    repository.insertBudget(Budget(category = "Food", icon = "fastfood", accent = "teal", limitAmount = 3000.0, spent = 1200.0))
                    repository.insertBudget(Budget(category = "Xerox", icon = "print", accent = "indigo", limitAmount = 600.0, spent = 320.0))
                    repository.insertBudget(Budget(category = "Subscriptions", icon = "subscriptions", accent = "orange", limitAmount = 1500.0, spent = 300.0))
                }
                "emperor" -> {
                    repository.saveUser(
                        User(
                            id = "user_default",
                            name = "Magnificent Emperor",
                            email = "emperor@spedex.empire",
                            plan = "Imperial Emperor",
                            avatarInitials = "👑",
                            memberSince = "May 2026"
                        )
                    )
                    // Spent: ₹12,450.0
                    repository.insertTransaction(Transaction(vendorName = "Feast Caterers", vendorUpi = "feast@paytm", description = "Roommate Feast Day Party", category = "Food", amount = 6500.0, status = "SUCCESS"))
                    repository.insertTransaction(Transaction(vendorName = "Hostel Room Advance", vendorUpi = "roomdeposit@oksbi", description = "Semester Room Booking", category = "Rent", amount = 5000.0, status = "SUCCESS"))
                    repository.insertTransaction(Transaction(vendorName = "High Speed Router Node", vendorUpi = "wifihouse@oksbi", description = "Semester internet router split", category = "Subscriptions", amount = 950.0, status = "SUCCESS"))
                    
                    repository.insertBudget(Budget(category = "Food", icon = "fastfood", accent = "teal", limitAmount = 8000.0, spent = 6500.0))
                    repository.insertBudget(Budget(category = "Rent", icon = "home", accent = "cyan", limitAmount = 5000.0, spent = 5000.0))
                    repository.insertBudget(Budget(category = "Subscriptions", icon = "subscriptions", accent = "orange", limitAmount = 2000.0, spent = 950.0))
                }
                "reset" -> {
                    // Re-seed standard original user
                    repository.seedInitialDataIfNeeded()
                }
            }
            repository.recalculateAllBudgets()
        }
    }
}

data class AnalyticsInsights(
    val highestSector: String,
    val busiestDay: String,
    val weekendRatioString: String,
    val weekdayRatio: Float // value between 0 and 1
)


