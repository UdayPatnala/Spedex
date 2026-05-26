package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.example.data.Budget
import com.example.ui.theme.*
import com.example.viewmodel.SpedexViewModel
import com.example.viewmodel.MedievalRankHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetsScreen(
    navController: NavController,
    viewModel: SpedexViewModel,
    modifier: Modifier = Modifier
) {
    val budgets by viewModel.allBudgets.collectAsStateWithLifecycle()
    val totalExpense by viewModel.totalExpense.collectAsStateWithLifecycle(initialValue = 0.0)
    
    // Calculate current rank to show in the budget ledger
    val rank = remember(totalExpense) { MedievalRankHelper.getRankForExpense(totalExpense) }

    var showCreateDialog by remember { mutableStateOf(false) }
    var selectedBudgetToEdit by remember { mutableStateOf<Budget?>(null) }

    // Dialog Input states
    var budgetCategory by remember { mutableStateOf("Food") }
    var budgetLimit by remember { mutableStateOf("") }
    var budgetAccent by remember { mutableStateOf("teal") }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    budgetCategory = "Food"
                    budgetLimit = ""
                    budgetAccent = "teal"
                    showCreateDialog = true
                },
                containerColor = TealAccent,
                contentColor = SpaceBlack,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .testTag("add_budget_fab")
                    .padding(bottom = 12.dp, end = 4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Budget")
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("New Budget", fontWeight = FontWeight.Bold)
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Screen Title Banner (Styled with Serif + Medieval context)
            item {
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = "Weekly & Monthly Budgets",
                    fontFamily = FontFamily.Serif,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = SpaceTextPrimary
                )
                
                Spacer(modifier = Modifier.height(6.dp))
                
                // Real-time sovereign rank feedback
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SoftBlushBg,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = rank.badgeIcon, fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Sovereign Tier: ${rank.title}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = BerryPrimary
                            )
                            Text(
                                text = "Your limits are protected under the estate authority.",
                                fontSize = 10.sp,
                                color = SpaceTextSecondary
                            )
                        }
                    }
                }
            }

            // Summary Stats Card
            item {
                val totalBudgetCap = budgets.sumOf { it.limitAmount }
                val overallProgressVal = if (totalBudgetCap > 0) (totalExpense / totalBudgetCap).coerceIn(0.0, 1.0) else 0.0

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = SpaceCard),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "Unplanned Wallet Cap", color = SpaceTextSecondary, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "₹%,.2f".format(totalBudgetCap - totalExpense),
                                color = if (totalExpense > totalBudgetCap) RoseAccent else TealAccent,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = "Spent ₹%,.0f of ₹%,.0f limit".format(totalExpense, totalBudgetCap),
                                color = SpaceTextSecondary,
                                fontSize = 11.sp
                            )
                        }

                        // Compact Percentage Gauge
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(SpaceCardAlt),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${((overallProgressVal) * 100).toInt()}%",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black,
                                color = if (overallProgressVal > 0.85) RoseAccent else TealAccent
                            )
                        }
                    }
                }
            }

            // Budgets list
            if (budgets.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.DataUsage,
                                contentDescription = null,
                                tint = SpaceCardAlt,
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                "No budget categories defined.",
                                color = SpaceTextSecondary,
                                fontSize = 14.sp
                            )
                            Text(
                                "Tap '+' below to define your first budget.",
                                color = SpaceTextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            } else {
                items(budgets) { budget ->
                    BudgetCard(
                        budget = budget,
                        onEdit = {
                            selectedBudgetToEdit = budget
                            budgetCategory = budget.category
                            budgetLimit = budget.limitAmount.toInt().toString()
                            budgetAccent = budget.accent
                        },
                        onDelete = {
                            viewModel.deleteBudget(budget)
                        }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        // Add / Create Budget Dialog
        if (showCreateDialog) {
            AlertDialog(
                onDismissRequest = { showCreateDialog = false },
                containerColor = SpaceCard,
                title = { Text("Define New Budget Cap", fontWeight = FontWeight.Bold, color = SpaceTextPrimary) },
                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Category Choice Picker
                        Text("Category Scope", fontSize = 12.sp, color = SpaceTextSecondary)
                        val categories = listOf("Food", "Xerox", "Transport", "Subscriptions", "Rent")
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            categories.forEach { cat ->
                                val isSelected = cat == budgetCategory
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) getCategoryAccent(cat) else SpaceCardAlt)
                                        .clickable { budgetCategory = cat }
                                        .padding(vertical = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = cat,
                                        fontSize = 11.sp,
                                        color = if (isSelected) SpaceBlack else SpaceTextSecondary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        // Limit amount
                        OutlinedTextField(
                            value = budgetLimit,
                            onValueChange = { if (it.isEmpty() || it.toIntOrNull() != null) budgetLimit = it },
                            placeholder = { Text("e.g. 1500") },
                            label = { Text("Limit Amount (₹)") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = SpaceTextPrimary,
                                unfocusedTextColor = SpaceTextPrimary,
                                focusedBorderColor = TealAccent,
                                unfocusedBorderColor = SpaceCardAlt,
                                focusedLabelColor = TealAccent,
                                unfocusedLabelColor = SpaceTextSecondary
                            ),
                            modifier = Modifier.fillMaxWidth().testTag("add_budget_input")
                        )

                        // Visual Accent Selector
                        Text("Visual Highlight Circle", fontSize = 12.sp, color = SpaceTextSecondary)
                        val accents = listOf("teal", "indigo", "rose", "orange", "cyan")
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            accents.forEach { acc ->
                                val isSelected = acc == budgetAccent
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(getAccentColor(acc))
                                        .clickable { budgetAccent = acc }
                                        .border(
                                            width = if (isSelected) 3.dp else 0.dp,
                                            color = if (isSelected) SpaceTextPrimary else Color.Transparent,
                                            shape = CircleShape
                                        )
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val cap = budgetLimit.toDoubleOrNull() ?: 0.0
                            if (cap > 0) {
                                viewModel.addBudget(budgetCategory, cap, budgetAccent)
                                showCreateDialog = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = TealAccent, contentColor = SpaceBlack),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("confirm_add_budget")
                    ) {
                        Text("Save Budget", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showCreateDialog = false }) {
                        Text("Cancel", color = SpaceTextSecondary)
                    }
                }
            )
        }

        // Edit Budget Dialog
        if (selectedBudgetToEdit != null) {
            val budget = selectedBudgetToEdit!!
            var editLimit by remember { mutableStateOf(budget.limitAmount.toInt().toString()) }

            AlertDialog(
                onDismissRequest = { selectedBudgetToEdit = null },
                containerColor = SpaceCard,
                title = { Text("Adjust ${budget.category} Budget", fontWeight = FontWeight.Bold, color = SpaceTextPrimary) },
                text = {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Currently Spent: ₹%,.2f".format(budget.spent),
                            fontSize = 12.sp,
                            color = SpaceTextSecondary,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        OutlinedTextField(
                            value = editLimit,
                            onValueChange = { if (it.isEmpty() || it.toIntOrNull() != null) editLimit = it },
                            label = { Text("New Limit (₹)") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = SpaceTextPrimary,
                                unfocusedTextColor = SpaceTextPrimary,
                                focusedBorderColor = TealAccent,
                                unfocusedBorderColor = SpaceCardAlt,
                                focusedLabelColor = TealAccent,
                                unfocusedLabelColor = SpaceTextSecondary
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val cap = editLimit.toDoubleOrNull() ?: 0.0
                            if (cap > 0) {
                                viewModel.updateBudgetLimit(budget.id, cap)
                                selectedBudgetToEdit = null
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = TealAccent, contentColor = SpaceBlack)
                    ) {
                        Text("Apply Change", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { selectedBudgetToEdit = null }) {
                        Text("Cancel", color = SpaceTextSecondary)
                    }
                }
            )
        }
    }
}

@Composable
fun BudgetCard(
    budget: Budget,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val progress = if (budget.limitAmount > 0) (budget.spent / budget.limitAmount).coerceIn(0.0, 1.0) else 0.0
    val isApproaching = progress >= 0.85

    // Flashing neon warning border if spent touches critical threshold
    val borderModifier = if (isApproaching) {
        Modifier.border(1.dp, RoseAccent, RoundedCornerShape(20.dp))
    } else {
        Modifier
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(borderModifier),
        colors = CardDefaults.cardColors(containerColor = SpaceCard),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(getAccentColor(budget.accent).copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = getCategoryIconSvg(budget.icon),
                            contentDescription = null,
                            tint = getAccentColor(budget.accent),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = budget.category,
                            fontWeight = FontWeight.Bold,
                            color = SpaceTextPrimary,
                            fontSize = 15.sp
                        )
                        Text(
                            text = "Utilization: ${((progress) * 100).toInt()}%",
                            fontSize = 10.sp,
                            color = if (isApproaching) RoseAccent else SpaceTextSecondary,
                            fontWeight = if (isApproaching) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }

                // Edit / Delete Icons Action Group
                Row {
                    IconButton(onClick = onEdit) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Limit", tint = SpaceTextSecondary, modifier = Modifier.size(18.dp))
                    }
                    IconButton(onClick = onDelete) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Budget", tint = RoseAccent.copy(alpha = 0.8f), modifier = Modifier.size(18.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Progress bar
            LinearProgressIndicator(
                progress = { progress.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(CircleShape),
                color = if (isApproaching) RoseAccent else getAccentColor(budget.accent),
                trackColor = SpaceCardAlt
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Spent ₹%,.2f".format(budget.spent),
                    fontSize = 11.sp,
                    color = SpaceTextSecondary,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Cap ₹%,.0f".format(budget.limitAmount),
                    fontSize = 11.sp,
                    color = SpaceTextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
