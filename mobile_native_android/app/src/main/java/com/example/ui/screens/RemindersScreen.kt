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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.data.Reminder
import com.example.ui.theme.*
import com.example.viewmodel.SpedexViewModel
import com.example.viewmodel.UpiInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemindersScreen(
    navController: NavController,
    viewModel: SpedexViewModel,
    modifier: Modifier = Modifier
) {
    val reminders by viewModel.allReminders.collectAsStateWithLifecycle()

    var showAddDialog by remember { mutableStateOf(false) }

    // Dialog Input states
    var reminderTitle by remember { mutableStateOf("") }
    var reminderSubtitle by remember { mutableStateOf("") }
    var reminderAmount by remember { mutableStateOf("") }
    var reminderDueDate by remember { mutableStateOf("") }
    var autopayEnabled by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    reminderTitle = ""
                    reminderSubtitle = ""
                    reminderAmount = ""
                    reminderDueDate = ""
                    autopayEnabled = false
                    showAddDialog = true
                },
                containerColor = TealAccent,
                contentColor = SpaceBlack,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .testTag("add_reminder_fab")
                    .padding(bottom = 12.dp, end = 4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.NotificationAdd, contentDescription = "Add Reminder")
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("New Reminder", fontWeight = FontWeight.Bold)
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
            // Header
            item {
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = "Subscriptions & Rent Reminders",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = SpaceTextPrimary
                )
                Text(
                    text = "Manage repetitive student service payments, laundry clubs, and split flats.",
                    fontSize = 12.sp,
                    color = SpaceTextSecondary
                )
            }

            // Reminders list
            if (reminders.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.NotificationsNone,
                                contentDescription = null,
                                tint = SpaceCardAlt,
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "No campus reminders scheduled.",
                                color = SpaceTextSecondary,
                                fontSize = 14.sp
                            )
                            Text(
                                "Tap '+' below to add an automated reminder.",
                                color = SpaceTextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            } else {
                items(reminders) { reminder ->
                    ReminderRow(
                        reminder = reminder,
                        onPayNow = {
                            // Inject UPI context and route immediately to Confirm
                            viewModel.setScannedUpi(
                                UpiInfo(
                                    upiId = "studentbills@upi", // Fallback system mock billing ID
                                    name = reminder.title,
                                    amount = reminder.amount
                                )
                            )
                            navController.navigate("pay_confirm")
                        },
                        onToggleAutopay = {
                            viewModel.updateReminderStatus(reminder.id, if (reminder.status == "PENDING") "PAID" else "PENDING")
                        },
                        onDelete = {
                            viewModel.deleteReminder(reminder)
                        }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        // Add Reminder Dialog
        if (showAddDialog) {
            AlertDialog(
                onDismissRequest = { showAddDialog = false },
                containerColor = SpaceCard,
                title = { Text("Schedule New Reminder", fontWeight = FontWeight.Bold, color = SpaceTextPrimary) },
                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Title
                        OutlinedTextField(
                            value = reminderTitle,
                            onValueChange = { reminderTitle = it },
                            placeholder = { Text("e.g. WiFi Bill Share") },
                            label = { Text("Reminder Title") },
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

                        // Subtitle
                        OutlinedTextField(
                            value = reminderSubtitle,
                            onValueChange = { reminderSubtitle = it },
                            placeholder = { Text("e.g. Split with room 302") },
                            label = { Text("Short Description / Handle") },
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

                        // Amount & Due Date row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = reminderAmount,
                                onValueChange = { if (it.isEmpty() || it.toDoubleOrNull() != null) reminderAmount = it },
                                placeholder = { Text("e.g. 250") },
                                label = { Text("Cost (₹)") },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = SpaceTextPrimary,
                                    unfocusedTextColor = SpaceTextPrimary,
                                    focusedBorderColor = TealAccent,
                                    unfocusedBorderColor = SpaceCardAlt,
                                    focusedLabelColor = TealAccent,
                                    unfocusedLabelColor = SpaceTextSecondary
                                ),
                                modifier = Modifier.weight(1f)
                            )

                            OutlinedTextField(
                                value = reminderDueDate,
                                onValueChange = { reminderDueDate = it },
                                placeholder = { Text("e.g. May 30") },
                                label = { Text("Due Date") },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = SpaceTextPrimary,
                                    unfocusedTextColor = SpaceTextPrimary,
                                    focusedBorderColor = TealAccent,
                                    unfocusedBorderColor = SpaceCardAlt,
                                    focusedLabelColor = TealAccent,
                                    unfocusedLabelColor = SpaceTextSecondary
                                ),
                                modifier = Modifier.weight(1f)
                            )
                        }

                        // Autopay switch
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(SpaceCardAlt)
                                .clickable { autopayEnabled = !autopayEnabled }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.Loop, contentDescription = null, tint = OrangeAccent)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Convenience pay split preference", fontSize = 11.sp, color = SpaceTextPrimary)
                            }
                            Switch(
                                checked = autopayEnabled,
                                onCheckedChange = { autopayEnabled = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = SpaceBlack,
                                    checkedTrackColor = TealAccent
                                )
                            )
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val cost = reminderAmount.toDoubleOrNull() ?: 0.0
                            if (reminderTitle.isNotBlank() && cost > 0 && reminderDueDate.isNotBlank()) {
                                viewModel.addReminder(
                                    title = reminderTitle,
                                    subtitle = reminderSubtitle,
                                    amount = cost,
                                    dueDate = reminderDueDate,
                                    autopay = autopayEnabled
                                )
                                showAddDialog = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = TealAccent, contentColor = SpaceBlack),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("confirm_add_reminder")
                    ) {
                        Text("Save Reminder", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddDialog = false }) {
                        Text("Cancel", color = SpaceTextSecondary)
                    }
                }
            )
        }
    }
}

@Composable
fun ReminderRow(
    reminder: Reminder,
    onPayNow: () -> Unit,
    onToggleAutopay: () -> Unit,
    onDelete: () -> Unit
) {
    val isPaid = reminder.status == "PAID"

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SpaceCard),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(modifier = Modifier.weight(1f)) {
                    // Left clock mark
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(if (isPaid) NeonEmerald.copy(alpha = 0.12f) else OrangeAccent.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isPaid) Icons.Default.EventAvailable else Icons.Default.NotificationsActive,
                            contentDescription = null,
                            tint = if (isPaid) NeonEmerald else OrangeAccent,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = reminder.title,
                            fontWeight = FontWeight.Bold,
                            color = SpaceTextPrimary,
                            fontSize = 14.sp
                        )
                        Text(
                            text = reminder.subtitle,
                            color = SpaceTextSecondary,
                            fontSize = 11.sp
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            Icon(imageVector = Icons.Default.CalendarToday, contentDescription = null, tint = TealAccent, modifier = Modifier.size(10.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "Due: ${reminder.dueDate}", fontSize = 10.sp, color = SpaceTextSecondary)
                        }
                    }
                }

                // Cost and Delete
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "₹%,.2f".format(reminder.amount),
                        fontWeight = FontWeight.ExtraBold,
                        color = SpaceTextPrimary,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = RoseAccent.copy(alpha = 0.7f), modifier = Modifier.size(16.dp))
                    }
                }
            }

            Divider(color = SpaceCardAlt)

            // Split Action triggers
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Autopay preferences visual badge
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isPaid,
                        onCheckedChange = { onToggleAutopay() },
                        colors = CheckboxDefaults.colors(
                            checkedColor = NeonEmerald,
                            checkmarkColor = SpaceBlack,
                            uncheckedColor = SpaceCardAlt
                        )
                    )
                    Text(
                        text = if (isPaid) "Marked PAID" else "Mark Paid",
                        fontSize = 11.sp,
                        color = if (isPaid) NeonEmerald else SpaceTextSecondary,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                if (!isPaid) {
                    Button(
                        onClick = onPayNow,
                        colors = ButtonDefaults.buttonColors(containerColor = TealAccent, contentColor = SpaceBlack),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("reminder_pay_now")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Pay Now", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(NeonEmerald.copy(alpha = 0.15f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text("SETTLED", color = NeonEmerald, fontSize = 9.sp, fontWeight = FontWeight.Black)
                    }
                }
            }
        }
    }
}
