package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.data.Trip
import com.example.ui.theme.*
import com.example.viewmodel.SpedexViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripsScreen(
    navController: NavController,
    viewModel: SpedexViewModel,
    modifier: Modifier = Modifier
) {
    val activeTrip by viewModel.activeTrip.collectAsStateWithLifecycle()
    val allTrips by viewModel.allTrips.collectAsStateWithLifecycle()
    val pastTrips = remember(allTrips) { allTrips.filter { it.status != "ACTIVE" } }

    var showNewTripDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Trips Ledger",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            color = SpaceTextPrimary
                        )
                        Text(
                            text = "Multi-Currency Forex & Travel Split",
                            fontSize = 11.sp,
                            color = SpaceTextSecondary
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = SpaceTextPrimary
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { showNewTripDialog = true },
                        modifier = Modifier.testTag("btn_new_trip")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddLocationAlt,
                            contentDescription = "New Trip",
                            tint = BerryPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SpaceBlack)
            )
        },
        containerColor = SpaceBlack,
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(4.dp))
            }

            // 1. Active Trip Hero Card
            item {
                Text(
                    text = "ACTIVE EXPEDITION",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp,
                    color = SpaceTextSecondary
                )
                Spacer(modifier = Modifier.height(8.dp))

                if (activeTrip != null) {
                    ActiveTripCard(
                        trip = activeTrip!!,
                        onCompleteTrip = { viewModel.completeTrip(activeTrip!!.id) }
                    )
                } else {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showNewTripDialog = true },
                        colors = CardDefaults.cardColors(containerColor = SpaceCard),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Luggage,
                                contentDescription = "No Active Trip",
                                tint = SpaceTextSecondary,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "No Active Trip In Progress",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = SpaceTextPrimary
                            )
                            Text(
                                text = "Start an expedition to track multi-currency budgets with offline forex conversion",
                                fontSize = 12.sp,
                                color = SpaceTextSecondary,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = { showNewTripDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = BerryPrimary),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.FlightTakeoff, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Launch New Trip", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // 2. Multi-Currency Quick Forex Reference
            item {
                Text(
                    text = "FOREX CONVERSION RATES (BASE: INR)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp,
                    color = SpaceTextSecondary
                )
                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = SpaceCard),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            ForexBadge(code = "USD", rate = "₹83.50", symbol = "$")
                            ForexBadge(code = "EUR", rate = "₹90.20", symbol = "€")
                            ForexBadge(code = "GBP", rate = "₹106.10", symbol = "£")
                            ForexBadge(code = "AED", rate = "₹22.75", symbol = "د.إ")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            ForexBadge(code = "SGD", rate = "₹62.40", symbol = "S$")
                            ForexBadge(code = "THB", rate = "₹2.28", symbol = "฿")
                            ForexBadge(code = "NPR", rate = "₹0.625", symbol = "₨")
                            ForexBadge(code = "JPY", rate = "₹0.54", symbol = "¥")
                        }
                    }
                }
            }

            // 3. Past Trips / Ledger History
            item {
                Text(
                    text = "EXPEDITION ARCHIVE (${pastTrips.size})",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp,
                    color = SpaceTextSecondary
                )
            }

            if (pastTrips.isEmpty()) {
                item {
                    Text(
                        text = "No completed trips yet. When you complete an active trip, its final forex summary will be archived here.",
                        fontSize = 13.sp,
                        color = SpaceTextSecondary,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                }
            } else {
                items(pastTrips, key = { it.id }) { trip ->
                    PastTripCard(trip = trip)
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    if (showNewTripDialog) {
        NewTripDialog(
            onDismiss = { showNewTripDialog = false },
            onConfirm = { name, budget, currency, rate ->
                viewModel.startTrip(name, budget, currency, rate)
                showNewTripDialog = false
            }
        )
    }
}

@Composable
fun ActiveTripCard(
    trip: Trip,
    onCompleteTrip: () -> Unit
) {
    val progress = if (trip.budget > 0) (trip.spent / trip.budget).coerceIn(0.0, 1.0).toFloat() else 0f
    val remaining = (trip.budget - trip.spent).coerceAtLeast(0.0)
    val foreignSpent = if (trip.exchangeRate > 0) trip.spent / trip.exchangeRate else trip.spent
    val foreignBudget = if (trip.exchangeRate > 0) trip.budget / trip.exchangeRate else trip.budget

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("active_trip_card"),
        colors = CardDefaults.cardColors(containerColor = SpaceCard),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(SoftBlushBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.FlightTakeoff,
                            contentDescription = null,
                            tint = BerryPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = trip.name,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = SpaceTextPrimary
                        )
                        Text(
                            text = "Currency: ${trip.currency} (1 ${trip.currency} = ₹${String.format(Locale.US, "%.2f", trip.exchangeRate)})",
                            fontSize = 11.sp,
                            color = SpaceTextSecondary
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = NeonEmerald.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = "ACTIVE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = NeonEmerald,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Metrics Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("SPENT", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = SpaceTextSecondary)
                    Text(
                        text = "₹${String.format(Locale.US, "%,.0f", trip.spent)}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = SpaceTextPrimary
                    )
                    if (trip.currency != "INR") {
                        Text(
                            text = "${trip.currency} ${String.format(Locale.US, "%.2f", foreignSpent)}",
                            fontSize = 11.sp,
                            color = BerryPrimary
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text("BUDGET", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = SpaceTextSecondary)
                    Text(
                        text = "₹${String.format(Locale.US, "%,.0f", trip.budget)}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = SpaceTextPrimary
                    )
                    if (trip.currency != "INR") {
                        Text(
                            text = "${trip.currency} ${String.format(Locale.US, "%.2f", foreignBudget)}",
                            fontSize = 11.sp,
                            color = SpaceTextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Progress bar
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = if (progress > 0.85f) RoseAccent else BerryPrimary,
                trackColor = SoftBlushBg
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Remaining: ₹${String.format(Locale.US, "%,.0f", remaining)}",
                    fontSize = 11.sp,
                    color = SpaceTextSecondary
                )
                Text(
                    text = "${(progress * 100).toInt()}% consumed",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (progress > 0.85f) RoseAccent else SpaceTextPrimary
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = onCompleteTrip,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("btn_complete_trip"),
                colors = ButtonDefaults.buttonColors(containerColor = SoftBlushBg, contentColor = BerryPrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.CheckCircleOutline, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Complete & Archive Expedition", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }
    }
}

@Composable
fun PastTripCard(trip: Trip) {
    val dateStr = remember(trip.createdAt) {
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.US)
        sdf.format(Date(trip.createdAt))
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SpaceCard),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(SpaceCardAlt),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = SpaceTextPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = trip.name,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = SpaceTextPrimary
                    )
                    Text(
                        text = "$dateStr • ${trip.currency}",
                        fontSize = 11.sp,
                        color = SpaceTextSecondary
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "₹${String.format(Locale.US, "%,.0f", trip.spent)}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = SpaceTextPrimary
                )
                Text(
                    text = "Budget: ₹${String.format(Locale.US, "%,.0f", trip.budget)}",
                    fontSize = 10.sp,
                    color = SpaceTextSecondary
                )
            }
        }
    }
}

@Composable
fun ForexBadge(code: String, rate: String, symbol: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(4.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = SoftBlushBg,
            modifier = Modifier.padding(bottom = 2.dp)
        ) {
            Text(
                text = "$symbol $code",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = BerryPrimary,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }
        Text(
            text = rate,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = SpaceTextPrimary
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTripDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, budget: Double, currency: String, exchangeRate: Double) -> Unit
) {
    var tripName by remember { mutableStateOf("") }
    var budgetStr by remember { mutableStateOf("") }
    var selectedCurrency by remember { mutableStateOf("INR") }
    var exchangeRateStr by remember { mutableStateOf("1.0") }

    val defaultRates = mapOf(
        "INR" to 1.0,
        "USD" to 83.50,
        "EUR" to 90.20,
        "GBP" to 106.10,
        "AED" to 22.75,
        "SGD" to 62.40,
        "THB" to 2.28,
        "NPR" to 0.625,
        "JPY" to 0.54
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Launch New Trip",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                color = SpaceTextPrimary
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = tripName,
                    onValueChange = { tripName = it },
                    label = { Text("Trip Name (e.g. Goa Outing, London)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = budgetStr,
                    onValueChange = { budgetStr = it },
                    label = { Text("Trip Budget (₹ INR)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "Select Trip Currency:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = SpaceTextSecondary
                )

                // Currency chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("INR", "USD", "EUR", "AED", "THB").forEach { curr ->
                        FilterChip(
                            selected = selectedCurrency == curr,
                            onClick = {
                                selectedCurrency = curr
                                exchangeRateStr = defaultRates[curr]?.toString() ?: "1.0"
                            },
                            label = { Text(curr, fontSize = 11.sp) }
                        )
                    }
                }

                OutlinedTextField(
                    value = exchangeRateStr,
                    onValueChange = { exchangeRateStr = it },
                    label = { Text("Exchange Rate (1 $selectedCurrency = X INR)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val budget = budgetStr.toDoubleOrNull() ?: 0.0
                    val rate = exchangeRateStr.toDoubleOrNull() ?: 1.0
                    if (tripName.isNotBlank() && budget > 0) {
                        onConfirm(tripName, budget, selectedCurrency, rate)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = BerryPrimary)
            ) {
                Text("Start Trip")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = SpaceTextSecondary)
            }
        },
        containerColor = SpaceCard
    )
}
