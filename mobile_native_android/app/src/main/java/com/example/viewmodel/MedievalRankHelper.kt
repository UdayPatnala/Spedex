package com.example.viewmodel

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.*

data class MedievalRank(
    val title: String,
    val description: String,
    val minExpenditure: Double,
    val maxExpenditure: Double,
    val rankBadgeColor: Color,
    val badgeIcon: String,
    val ruleAuthority: String,
    val nextRankThreshold: Double,
    val prestigeLevel: String
)

object MedievalRankHelper {
    val ranks = listOf(
        MedievalRank(
            title = "Emperor",
            description = "Absolute sovereign of the Spedex Realm. Your grand spending dictates imperial trade laws and rules all student commerce.",
            minExpenditure = 10000.0,
            maxExpenditure = Double.MAX_VALUE,
            rankBadgeColor = BerryPrimary,
            badgeIcon = "✨👑✨",
            ruleAuthority = "Decree: 100% Hostel Tax Exemption",
            nextRankThreshold = 10000.0,
            prestigeLevel = "Imperial Overlord"
        ),
        MedievalRank(
            title = "King",
            description = "Sovereign ruler of budgets. All vassal categories bow down to your monthly limit allocations.",
            minExpenditure = 8500.0,
            maxExpenditure = 10000.0,
            rankBadgeColor = BerryPrimary,
            badgeIcon = "👑",
            ruleAuthority = "Decree: Priority Canteen Feasting",
            nextRankThreshold = 10000.0,
            prestigeLevel = "Sovereign"
        ),
        MedievalRank(
            title = "Marques",
            description = "Distinguished warden of the outer subscription borders. Your administrative budget control is formidable.",
            minExpenditure = 7000.0,
            maxExpenditure = 8500.0,
            rankBadgeColor = IndigoAccent,
            badgeIcon = "🦅",
            ruleAuthority = "Decree: Carriage Fast-Pass Privilege",
            nextRankThreshold = 8500.0,
            prestigeLevel = "Grand Peer"
        ),
        MedievalRank(
            title = "Duke",
            description = "Grand high noble of peerage. Your split-bill commands are law among common hostel roommates.",
            minExpenditure = 5500.0,
            maxExpenditure = 7000.0,
            rankBadgeColor = OrangeAccent,
            badgeIcon = "🦁",
            ruleAuthority = "Decree: Unlimited Room Cleaning",
            nextRankThreshold = 7000.0,
            prestigeLevel = "High Nobility"
        ),
        MedievalRank(
            title = "Viscount",
            description = "Landed lord of xerox copy shops. Your command of papers and files is legendary.",
            minExpenditure = 4000.0,
            maxExpenditure = 5500.0,
            rankBadgeColor = CyanAccent,
            badgeIcon = "🗡️",
            ruleAuthority = "Decree: Complimentary Golden Stamp",
            nextRankThreshold = 5500.0,
            prestigeLevel = "High Gentry"
        ),
        MedievalRank(
            title = "Count",
            description = "Noble overseer of provincial transport budgets. Your rickshaw caravans travel far and wide.",
            minExpenditure = 2500.0,
            maxExpenditure = 4000.0,
            rankBadgeColor = RoseAccent,
            badgeIcon = "🏰",
            ruleAuthority = "Decree: Rickshaw Front-Seat Reserved",
            nextRankThreshold = 4000.0,
            prestigeLevel = "Gentry"
        ),
        MedievalRank(
            title = "Lord",
            description = "A respected noble of the local hall. You maintain a stable, well-organized estate ledger.",
            minExpenditure = 1000.0,
            maxExpenditure = 2500.0,
            rankBadgeColor = NeonEmerald,
            badgeIcon = "🛡️",
            ruleAuthority = "Decree: Fresh Laundry Priority",
            nextRankThreshold = 2500.0,
            prestigeLevel = "Lesser Nobility"
        ),
        MedievalRank(
            title = "Village Chief",
            description = "Respected protector of a peaceful local commune. Strictly avoiding high debt thresholds.",
            minExpenditure = 500.0,
            maxExpenditure = 1000.0,
            rankBadgeColor = OrangeAccent,
            badgeIcon = "🛖",
            ruleAuthority = "Decree: Double Samosa Portion",
            nextRankThreshold = 1000.0,
            prestigeLevel = "Elder Counselor"
        ),
        MedievalRank(
            title = "Merchant",
            description = "A thriving tradesperson of student services. Keenly bartering coins for high-quality Xerox and Chai.",
            minExpenditure = 150.0,
            maxExpenditure = 500.0,
            rankBadgeColor = BerryPrimary,
            badgeIcon = "🪙",
            ruleAuthority = "Decree: 10% Chai Stall Rebate",
            nextRankThreshold = 500.0,
            prestigeLevel = "Guild Trader"
        ),
        MedievalRank(
            title = "Peasant",
            description = "A quiet wanderer of the outer wallet woods. Conserving gold with supreme, self-reliant discipline.",
            minExpenditure = 0.0,
            maxExpenditure = 150.0,
            rankBadgeColor = SpaceTextSecondary,
            badgeIcon = "🌾",
            ruleAuthority = "Decree: Reserved Stone Circle Seat",
            nextRankThreshold = 150.0,
            prestigeLevel = "Commoner"
        )
    )

    fun getRankForExpense(expense: Double): MedievalRank {
        return ranks.find { expense >= it.minExpenditure } ?: ranks.last()
    }
}
