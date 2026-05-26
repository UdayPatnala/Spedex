package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.data.Transaction
import com.example.data.Vendor
import com.example.ui.theme.*
import com.example.viewmodel.SpedexViewModel
import com.example.viewmodel.UpiInfo
import com.example.viewmodel.MedievalRankHelper
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: SpedexViewModel,
    modifier: Modifier = Modifier
) {
    val user by viewModel.user.collectAsStateWithLifecycle()
    val quickPayVendors by viewModel.quickPayVendors.collectAsStateWithLifecycle()
    val transactions by viewModel.allTransactions.collectAsStateWithLifecycle()
    val budgets by viewModel.allBudgets.collectAsStateWithLifecycle()
    val totalExpense by viewModel.totalExpense.collectAsStateWithLifecycle(initialValue = 0.0)

    val totalBudgetCap = remember(budgets) { budgets.sumOf { it.limitAmount } }
    val progressFraction = remember(totalExpense, totalBudgetCap) {
        if (totalBudgetCap > 0) (totalExpense / totalBudgetCap).coerceIn(0.0, 1.0) else 0.0
    }

    var selectedVendorForQuickPay by remember { mutableStateOf<Vendor?>(null) }
    var showQuickPaySheet by remember { mutableStateOf(false) }

    // Derive medieval rank dynamically
    val rank = remember(totalExpense) {
        MedievalRankHelper.getRankForExpense(totalExpense)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("scanner") },
                containerColor = BerryPrimary,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .testTag("scan_qr_fab")
                    .padding(bottom = 12.dp, end = 4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.QrCodeScanner, contentDescription = "Scan QR")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Scan QR", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        },
        containerColor = SpaceBlack,
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Elegant Editorial Header (Matches Logo in Images)
            item {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Curved "S" emblem (Matches web image)
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(BerryPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "S",
                                color = Color.White,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Text(
                                text = "Spedex",
                                fontSize = 21.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif,
                                color = SpaceTextPrimary
                            )
                            Text(
                                text = "SMART WALLET",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 1.sp,
                                color = SpaceTextSecondary
                            )
                        }
                    }

                    // Member profile badge
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = SoftBlushBg,
                            modifier = Modifier.clickable { navController.navigate("analytics") }
                        ) {
                            Text(
                                text = user?.plan?.uppercase() ?: "PREMIUM",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                color = BerryPrimary,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }

                        // Avatar Initials
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(SoftBlushBg)
                                .clickable { navController.navigate("analytics") },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = user?.avatarInitials ?: "LU",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                color = BerryPrimary
                            )
                        }
                    }
                }
            }

            // Welcome Hero Banner Box - Editorial Light Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = SpaceCard),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text(
                            text = "Take control of\nyour spending,\none tap at a time.",
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            lineHeight = 32.sp,
                            color = SpaceTextPrimary
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Spedex connects your frequent payees, monthly rent reminders, and spending trends into a single tap interface.",
                            fontSize = 12.sp,
                            lineHeight = 17.sp,
                            color = SpaceTextSecondary
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "MONTHLY SPENDING",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 0.5.sp,
                                    color = SpaceTextSecondary
                                )
                                Text(
                                    text = "₹%,.2f".format(totalExpense),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black,
                                    color = SpaceTextPrimary
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "BUDGET PACING",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 0.5.sp,
                                    color = SpaceTextSecondary
                                )
                                Text(
                                    text = "${((progressFraction) * 100).toInt()}% used",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black,
                                    color = if (progressFraction > 0.8) RoseAccent else TealAccent
                                )
                            }
                        }
                    }
                }
            }

            // Interactive Demo Profile Selector
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Demo Sandbox Profiles",
                        fontFamily = FontFamily.Serif,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = SpaceTextPrimary
                    )
                    Text(
                        text = "Choose a demo environment to instantly showcase the medieval ranking system:",
                        fontSize = 11.sp,
                        color = SpaceTextSecondary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val currentInitials = user?.avatarInitials ?: ""
                        
                        item {
                            DemoSelectorChip(
                                title = "🌾 Peasant Al",
                                selected = currentInitials == "AL",
                                onClick = { viewModel.loadDemoProfile("PEASANT") }
                            )
                        }

                        item {
                            DemoSelectorChip(
                                title = "🪙 Merchant",
                                selected = currentInitials == "MC",
                                onClick = { viewModel.loadDemoProfile("MERCHANT") }
                            )
                        }

                        item {
                            DemoSelectorChip(
                                title = "🛖 Chief Al",
                                selected = currentInitials == "VC",
                                onClick = { viewModel.loadDemoProfile("CHIEF") }
                            )
                        }

                        item {
                            DemoSelectorChip(
                                title = "🛡️ Lord Uday",
                                selected = currentInitials == "LU",
                                onClick = { viewModel.loadDemoProfile("LORD") }
                            )
                        }

                        item {
                            DemoSelectorChip(
                                title = "👑 Emperor",
                                selected = currentInitials == "👑",
                                onClick = { viewModel.loadDemoProfile("EMPEROR") }
                            )
                        }
                    }
                }
            }

            // UNIQUE FEATURE: Medieval Rating / Rank Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = SpaceCard),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(CircleShape)
                                        .background(SoftBlushBg),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = rank.badgeIcon,
                                        fontSize = 24.sp
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column {
                                    Text(
                                        text = "MEDIEVAL STATE TIER",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Black,
                                        letterSpacing = 0.5.sp,
                                        color = SpaceTextSecondary
                                    )
                                    Text(
                                        text = rank.title,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Black,
                                        color = TealAccent,
                                        fontFamily = FontFamily.Serif
                                    )
                                }
                            }

                            // Rule Authority Decree Tag
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = SoftBlushBg
                            ) {
                                Text(
                                    text = "ROYAL DECREE",
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Black,
                                    color = BerryPrimary,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Hierarchy explanation
                        Text(
                            text = rank.description,
                            fontSize = 11.sp,
                            lineHeight = 16.sp,
                            color = SpaceTextSecondary
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Rule authority
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = SoftBlushBg,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Gavel,
                                    contentDescription = null,
                                    tint = TealAccent,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = rank.ruleAuthority,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TealAccent
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Next tier target and progress
                        val nextRank = MedievalRankHelper.ranks
                            .filter { it.minExpenditure > rank.minExpenditure }
                            .minByOrNull { it.minExpenditure }

                        if (nextRank != null) {
                            val nextThreshold = nextRank.minExpenditure
                            val gap = nextThreshold - totalExpense
                            val remainingFraction = (totalExpense / nextThreshold).coerceIn(0.0, 1.0)

                            Column(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Progress to ${nextRank.title} (${nextRank.badgeIcon})",
                                        fontSize = 10.sp,
                                        color = SpaceTextSecondary,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = "₹%,.0f more spending required".format(gap),
                                        fontSize = 9.sp,
                                        color = TealAccent,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                LinearProgressIndicator(
                                    progress = { remainingFraction.toFloat() },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp)
                                        .clip(CircleShape),
                                    color = TealAccent,
                                    trackColor = SpaceCardAlt
                                )
                            }
                        } else {
                            // Emperor highest rank reached
                            Text(
                                text = "👑 Absolutely Golden! You have expanded into the ultimate Emperor domain. No higher rank is recordable.",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = TealAccent
                            )
                        }
                    }
                }
            }

            // Beautiful Deep Berry security alert block (Matches the image's "Peak day", "Security" box)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Deep plum left card
                    Card(
                        modifier = Modifier
                            .weight(1.5f)
                            .height(84.dp),
                        colors = CardDefaults.cardColors(containerColor = DeepPlumBg),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Active Rank Title",
                                fontSize = 10.sp,
                                color = SoftBlushBg.copy(alpha = 0.7f),
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = rank.title.uppercase(Locale.getDefault()),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = WhitePlumText
                            )
                        }
                    }

                    // Security Card
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(84.dp),
                        colors = CardDefaults.cardColors(containerColor = SoftBlushBg),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Security Status",
                                fontSize = 10.sp,
                                color = BerryPrimary.copy(alpha = 0.7f),
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "UPI Secured",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = BerryPrimary
                            )
                        }
                    }
                }
            }

            // Quick Pay Row Header - Serif
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Quick Pay",
                        fontFamily = FontFamily.Serif,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = SpaceTextPrimary
                    )
                    Text(
                        text = "${quickPayVendors.size} Favourite Payees",
                        fontSize = 11.sp,
                        color = BerryPrimary,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(SoftBlushBg)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // Quick Pay Roll
            item {
                if (quickPayVendors.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(84.dp)
                            .background(SpaceCard, RoundedCornerShape(16.dp))
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No Quick Pay vendors yet. Scan a QR code to list them!",
                            color = SpaceTextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                } else {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("quick_pay_row"),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(quickPayVendors) { vendor ->
                            Column(
                                modifier = Modifier
                                    .clickable {
                                        selectedVendorForQuickPay = vendor
                                        showQuickPaySheet = true
                                    }
                                    .width(72.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(54.dp)
                                        .clip(CircleShape)
                                        .background(SoftBlushBg),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = getCategoryIconSvg(vendor.icon),
                                        contentDescription = vendor.name,
                                        tint = TealAccent,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = vendor.name,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SpaceTextPrimary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "₹%,.0f".format(vendor.defaultAmount),
                                    fontSize = 9.sp,
                                    color = SpaceTextSecondary,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }

            // Transactions Header - Serif
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recent Transactions",
                        fontFamily = FontFamily.Serif,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = SpaceTextPrimary
                    )
                    Text(
                        text = "View Analytics",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = SpaceTextSecondary,
                        modifier = Modifier
                            .clickable { navController.navigate("analytics") }
                            .padding(vertical = 4.dp, horizontal = 4.dp)
                    )
                }
            }

            // Transactions Items List
            if (transactions.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.ReceiptLong,
                                contentDescription = null,
                                tint = SpaceCardAlt,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "No transactions found yet",
                                color = SpaceTextSecondary,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            } else {
                items(transactions.take(5)) { tx ->
                    TransactionRow(transaction = tx)
                }
            }

            // Bottom Spacing
            item {
                Spacer(modifier = Modifier.height(90.dp))
            }
        }

        // Quick Pay Dialog Sheet
        if (showQuickPaySheet && selectedVendorForQuickPay != null) {
            val vendor = selectedVendorForQuickPay!!
            var payAmount by remember { mutableStateOf(vendor.defaultAmount.toString()) }

            AlertDialog(
                onDismissRequest = {
                    showQuickPaySheet = false
                    selectedVendorForQuickPay = null
                },
                containerColor = SpaceCard,
                textContentColor = SpaceTextPrimary,
                shape = RoundedCornerShape(24.dp),
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(SoftBlushBg),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = getCategoryIconSvg(vendor.icon),
                                contentDescription = null,
                                tint = TealAccent,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Pay ${vendor.name}",
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Serif,
                            fontSize = 20.sp,
                            color = SpaceTextPrimary
                        )
                        Text(
                            text = "UPI ID: ${vendor.upiHandle}",
                            fontSize = 11.sp,
                            color = SpaceTextSecondary
                        )
                    }
                },
                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Enter payment amount:",
                            fontSize = 13.sp,
                            color = SpaceTextSecondary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        
                        OutlinedTextField(
                            value = payAmount,
                            onValueChange = { if (it.isEmpty() || it.toDoubleOrNull() != null) payAmount = it },
                            placeholder = { Text("0.00", color = SpaceTextSecondary) },
                            prefix = { Text("₹ ", fontWeight = FontWeight.Bold, color = SpaceTextPrimary) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = SpaceTextPrimary,
                                unfocusedTextColor = SpaceTextPrimary,
                                focusedBorderColor = TealAccent,
                                unfocusedBorderColor = SpaceCardAlt,
                                cursorColor = TealAccent
                            ),
                            textStyle = LocalTextStyle.current.copy(fontSize = 22.sp, fontWeight = FontWeight.Bold),
                            maxLines = 1,
                            modifier = Modifier.width(180.dp)
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val amount = payAmount.toDoubleOrNull() ?: 0.0
                            if (amount > 0) {
                                showQuickPaySheet = false
                                viewModel.setScannedUpi(
                                    UpiInfo(
                                        upiId = vendor.upiHandle,
                                        name = vendor.name,
                                        amount = amount
                                    )
                                )
                                selectedVendorForQuickPay = null
                                navController.navigate("pay_confirm")
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BerryPrimary, contentColor = Color.White),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Initiate Pay", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showQuickPaySheet = false
                            selectedVendorForQuickPay = null
                        }
                    ) {
                        Text("Cancel", color = SpaceTextSecondary)
                    }
                }
            )
        }
    }
}

@Composable
fun DemoSelectorChip(
    title: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (selected) BerryPrimary else SpaceCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, if (selected) BerryPrimary else SpaceCardAlt),
        modifier = modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                fontSize = 10.sp,
                fontWeight = if (selected) FontWeight.Black else FontWeight.Bold,
                color = if (selected) Color.White else SpaceTextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun TransactionRow(transaction: Transaction) {
    val dateString = remember(transaction.occurredAt) {
        val sdf = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
        sdf.format(Date(transaction.occurredAt))
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = SpaceCard),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Category icon with specific background mapping
                val accent = getCategoryAccent(transaction.category)
                val icon = getCategoryIconName(transaction.category)

                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(SoftBlushBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = getCategoryIconSvg(icon),
                        contentDescription = null,
                        tint = TealAccent,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = transaction.vendorName,
                        fontWeight = FontWeight.Bold,
                        color = SpaceTextPrimary,
                        fontSize = 13.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "$dateString • ${transaction.category}",
                        fontSize = 11.sp,
                        color = SpaceTextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text(
                    text = "-₹%,.2f".format(transaction.amount),
                    fontWeight = FontWeight.Black,
                    color = SpaceTextPrimary,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(4.dp))

                // Custom high contrast status pill
                val (color, label) = when (transaction.status) {
                    "SUCCESS" -> NeonEmerald to "SUCCESS"
                    "PENDING" -> OrangeAccent to "PENDING"
                    else -> RoseAccent to "FAILED"
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(color.copy(alpha = 0.12f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = label,
                        color = color,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }
    }
}

// Global Mappings helper
fun getAccentColor(name: String): Color {
    return when (name.lowercase()) {
        "teal" -> TealAccent
        "indigo" -> IndigoAccent
        "rose" -> RoseAccent
        "orange" -> OrangeAccent
        "cyan" -> CyanAccent
        "emerald" -> NeonEmerald
        else -> TealAccent
    }
}

fun getCategoryIconSvg(icon: String): ImageVector {
    return when (icon.lowercase()) {
        "fastfood", "food" -> Icons.Default.Fastfood
        "print", "xerox" -> Icons.Default.Print
        "directions_car", "transport" -> Icons.Default.DirectionsCar
        "subscriptions" -> Icons.Default.Subscriptions
        "home", "rent" -> Icons.Default.Home
        "coffee" -> Icons.Default.Coffee
        "store" -> Icons.Default.Store
        else -> Icons.Default.ReceiptLong
    }
}

fun getCategoryAccent(category: String): Color {
    return when (category.lowercase()) {
        "food" -> TealAccent
        "xerox" -> IndigoAccent
        "transport" -> RoseAccent
        "subscriptions" -> OrangeAccent
        "rent" -> CyanAccent
        else -> NeonEmerald
    }
}

fun getCategoryIconName(category: String): String {
    return when (category.lowercase()) {
        "food" -> "fastfood"
        "xerox" -> "print"
        "transport" -> "directions_car"
        "subscriptions" -> "subscriptions"
        "rent" -> "home"
        else -> "receipt"
    }
}
