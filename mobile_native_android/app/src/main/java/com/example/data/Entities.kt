package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: String = "user_default",
    val name: String,
    val email: String,
    val plan: String = "Student Free",
    val avatarInitials: String = "JD",
    val profilePictureUrl: String = "",
    val memberSince: String = "May 2026"
)

@Entity(tableName = "vendors")
data class Vendor(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val category: String, // e.g. "Food", "Xerox", "Transport", "Subscriptions", "Rent"
    val icon: String = "store", // Icon identifier
    val accent: String = "teal", // Color identifier: "teal", "orange", "indigo", "rose", "emerald"
    val upiHandle: String, // UPI ID, e.g. canteen@upi
    val defaultAmount: Double = 0.0,
    val isQuickPay: Boolean = false
)

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val vendorName: String,
    val vendorUpi: String,
    val description: String,
    val category: String,
    val amount: Double,
    val direction: String = "OUT", // "IN" or "OUT"
    val paymentMethod: String = "UPI Deep Link",
    val accountLabel: String = "Primary (Hostel)",
    val status: String, // "PREPARE" -> "PENDING" -> "SUCCESS" or "FAILED"
    val externalReference: String = "",
    val occurredAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "budgets")
data class Budget(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val category: String,
    val icon: String,
    val accent: String, // color anchor
    val limitAmount: Double,
    val spent: Double = 0.0
)

@Entity(tableName = "reminders")
data class Reminder(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val subtitle: String,
    val amount: Double,
    val dueDate: String, // e.g., "Every 5th", "May 30"
    val autopayEnabled: Boolean = false,
    val status: String = "PENDING" // "PENDING", "PAID"
)

@Entity(tableName = "trips")
data class Trip(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val budget: Double,
    val currency: String = "INR",
    val exchangeRate: Double = 1.0,
    val status: String = "ACTIVE", // "ACTIVE" or "COMPLETED"
    val totalSpend: Double = 0.0,
    val createdAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null
)

@Entity(tableName = "privacy_settings")
data class PrivacySettings(
    @PrimaryKey val id: String = "privacy_default",
    val isMinor: Boolean = false,
    val age: Int = 20,
    val essentialConsent: Boolean = true,
    val analyticsConsent: Boolean = false,
    val marketingConsent: Boolean = false,
    val locationConsent: Boolean = false,
    val aiConsent: Boolean = false,
    val guardianName: String = "",
    val guardianEmail: String = "",
    val guardianConsentStatus: String = "NOT_REQUIRED", // "NOT_REQUIRED", "PENDING", "VERIFIED"
    val lastExportTimestamp: Long = 0L,
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "privacy_audit_logs")
data class PrivacyAuditLogItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val eventType: String,
    val details: String,
    val timestamp: Long = System.currentTimeMillis()
)

