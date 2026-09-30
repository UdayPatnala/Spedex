package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    fun getUserById(id: String = "user_default"): Flow<User?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateUser(user: User)
}

@Dao
interface VendorDao {
    @Query("SELECT * FROM vendors ORDER BY id DESC")
    fun getAllVendors(): Flow<List<Vendor>>

    @Query("SELECT * FROM vendors WHERE isQuickPay = 1")
    fun getQuickPayVendors(): Flow<List<Vendor>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVendor(vendor: Vendor): Long

    @Update
    suspend fun updateVendor(vendor: Vendor)

    @Delete
    suspend fun deleteVendor(vendor: Vendor)

    @Query("DELETE FROM vendors")
    suspend fun deleteAllVendors()

    @Query("SELECT * FROM vendors WHERE upiHandle = :upiHandle LIMIT 1")
    suspend fun getVendorByUpi(upiHandle: String): Vendor?
}

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY occurredAt DESC")
    fun getAllTransactions(): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE occurredAt >= :sinceTimestamp")
    fun getRecentTransactions(sinceTimestamp: Long): Flow<List<Transaction>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction): Long

    @Update
    suspend fun updateTransaction(transaction: Transaction)

    @Query("UPDATE transactions SET status = :status WHERE id = :id")
    suspend fun updateTransactionStatus(id: Int, status: String)

    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

    @Query("DELETE FROM transactions")
    suspend fun deleteAllTransactions()
}

@Dao
interface BudgetDao {
    @Query("SELECT * FROM budgets ORDER BY id DESC")
    fun getAllBudgets(): Flow<List<Budget>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudget(budget: Budget): Long

    @Update
    suspend fun updateBudget(budget: Budget)

    @Delete
    suspend fun deleteBudget(budget: Budget)

    @Query("DELETE FROM budgets")
    suspend fun deleteAllBudgets()

    @Query("UPDATE budgets SET spent = :spent WHERE category = :category")
    suspend fun updateBudgetSpent(category: String, spent: Double)
}

@Dao
interface ReminderDao {
    @Query("SELECT * FROM reminders ORDER BY id DESC")
    fun getAllReminders(): Flow<List<Reminder>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: Reminder): Long

    @Update
    suspend fun updateReminder(reminder: Reminder)

    @Delete
    suspend fun deleteReminder(reminder: Reminder)

    @Query("DELETE FROM reminders")
    suspend fun deleteAllReminders()
}

@Dao
interface TripDao {
    @Query("SELECT * FROM trips WHERE status = 'ACTIVE' LIMIT 1")
    fun getActiveTrip(): Flow<Trip?>

    @Query("SELECT * FROM trips ORDER BY createdAt DESC")
    fun getAllTrips(): Flow<List<Trip>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrip(trip: Trip): Long

    @Update
    suspend fun updateTrip(trip: Trip)

    @Query("UPDATE trips SET status = 'COMPLETED', completedAt = :completedAt WHERE status = 'ACTIVE'")
    suspend fun closeAllActiveTrips(completedAt: Long = System.currentTimeMillis())

    @Query("UPDATE trips SET totalSpend = totalSpend + :amount WHERE id = :tripId")
    suspend fun addSpendToTrip(tripId: Int, amount: Double)

    @Query("DELETE FROM trips")
    suspend fun deleteAllTrips()
}

@Dao
interface PrivacyDao {
    @Query("SELECT * FROM privacy_settings WHERE id = :id LIMIT 1")
    fun getPrivacySettings(id: String = "privacy_default"): Flow<PrivacySettings?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdatePrivacySettings(settings: PrivacySettings)

    @Query("SELECT * FROM privacy_audit_logs ORDER BY timestamp DESC")
    fun getAllAuditLogs(): Flow<List<PrivacyAuditLogItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuditLog(log: PrivacyAuditLogItem): Long

    @Query("DELETE FROM privacy_audit_logs")
    suspend fun deleteAllAuditLogs()
}

