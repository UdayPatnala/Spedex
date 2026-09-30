package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.data.PrivacyAuditLogItem
import com.example.ui.theme.*
import com.example.viewmodel.SpedexViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyCenterScreen(
    navController: NavController,
    viewModel: SpedexViewModel,
    modifier: Modifier = Modifier
) {
    val privacySettings by viewModel.privacySettings.collectAsStateWithLifecycle()
    val auditLogs by viewModel.privacyAuditLogs.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()
    val clipboardManager = LocalClipboardManager.current

    // Local mutable state for consent toggles
    var analyticsConsent by remember(privacySettings) { mutableStateOf(privacySettings?.analyticsConsent ?: false) }
    var marketingConsent by remember(privacySettings) { mutableStateOf(privacySettings?.marketingConsent ?: false) }
    var locationConsent by remember(privacySettings) { mutableStateOf(privacySettings?.locationConsent ?: false) }
    var aiConsent by remember(privacySettings) { mutableStateOf(privacySettings?.aiConsent ?: false) }

    // Dialog states
    var showGuardianDialog by remember { mutableStateOf(false) }
    var showExportDialog by remember { mutableStateOf(false) }
    var exportJsonContent by remember { mutableStateOf("") }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var deleteConfirmationText by remember { mutableStateOf("") }

    val isMinor = privacySettings?.isMinor ?: false
    val guardianStatus = privacySettings?.guardianConsentStatus ?: "NOT_REQUIRED"

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Privacy & Consent",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            color = SpaceTextPrimary
                        )
                        Text(
                            text = "DPDP Act 2023 Trust Architecture",
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

            // 1. DPDP Compliance & Minor Safety Shield Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = SpaceCard),
                    shape = RoundedCornerShape(18.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(if (isMinor) SoftBlushBg else NeonEmerald.copy(alpha = 0.12f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isMinor) Icons.Default.Shield else Icons.Default.VerifiedUser,
                                    contentDescription = null,
                                    tint = if (isMinor) BerryPrimary else NeonEmerald,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = if (isMinor) "Minor Financial Learning Shield" else "Adult Account Verified",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SpaceTextPrimary
                                )
                                Text(
                                    text = if (isMinor) "Age: ${privacySettings?.age ?: 16} • Section 9 Minor Protections" else "Age: ${privacySettings?.age ?: 20} • Full Sovereign Financial Control",
                                    fontSize = 12.sp,
                                    color = SpaceTextSecondary
                                )
                            }
                        }

                        if (isMinor) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = SoftBlushBg,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = "Parental Consent Status:",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = SpaceTextPrimary
                                        )
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = when (guardianStatus) {
                                                "VERIFIED" -> NeonEmerald.copy(alpha = 0.2f)
                                                "PENDING" -> OrangeAccent.copy(alpha = 0.2f)
                                                else -> SpaceCardAlt
                                            }
                                        ) {
                                            Text(
                                                text = guardianStatus,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = when (guardianStatus) {
                                                    "VERIFIED" -> NeonEmerald
                                                    "PENDING" -> OrangeAccent
                                                    else -> SpaceTextSecondary
                                                },
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                            )
                                        }
                                    }

                                    if (privacySettings?.guardianName?.isNotBlank() == true) {
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Guardian: ${privacySettings?.guardianName} (${privacySettings?.guardianEmail})",
                                            fontSize = 11.sp,
                                            color = SpaceTextSecondary
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Button(
                                            onClick = { showGuardianDialog = true },
                                            colors = ButtonDefaults.buttonColors(containerColor = BerryPrimary),
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text("Update Guardian", fontSize = 11.sp)
                                        }

                                        if (guardianStatus != "VERIFIED") {
                                            OutlinedButton(
                                                onClick = { viewModel.verifyGuardianConsent() },
                                                shape = RoundedCornerShape(10.dp),
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                Text("Simulate Verify", fontSize = 11.sp, color = BerryPrimary)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 2. Granular Consent Controls (DPDP Section 6)
            item {
                Text(
                    text = "PURPOSE-SEPARATED CONSENTS (DPDP SEC. 6)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp,
                    color = SpaceTextSecondary
                )
                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = SpaceCard),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Essential Processing (Immutable)
                        ConsentRow(
                            title = "Essential UPI Settlement",
                            description = "Required to execute peer payments, maintain ledger history, and process QR tokens.",
                            checked = true,
                            enabled = false,
                            onCheckedChange = {}
                        )

                        Divider(modifier = Modifier.padding(vertical = 10.dp), color = SpaceBlack)

                        // Analytics
                        ConsentRow(
                            title = "Analytics & Usage Patterns",
                            description = "Aggregated, non-identifiable usage statistics to optimize budget category recommendations.",
                            checked = analyticsConsent,
                            enabled = true,
                            onCheckedChange = { analyticsConsent = it }
                        )

                        Divider(modifier = Modifier.padding(vertical = 10.dp), color = SpaceBlack)

                        // Marketing
                        ConsentRow(
                            title = "Promotions & Merchant Offers",
                            description = "Personalized discounts and special deals from verified local campus vendors.",
                            checked = marketingConsent,
                            enabled = true,
                            onCheckedChange = { marketingConsent = it }
                        )

                        Divider(modifier = Modifier.padding(vertical = 10.dp), color = SpaceBlack)

                        // Location
                        ConsentRow(
                            title = "Precise Geolocation",
                            description = "Detect nearby campus stalls and autofill vendor UPI IDs on proximity.",
                            checked = locationConsent,
                            enabled = true,
                            onCheckedChange = { locationConsent = it }
                        )

                        Divider(modifier = Modifier.padding(vertical = 10.dp), color = SpaceBlack)

                        // AI Insights
                        ConsentRow(
                            title = "AI Forecasting & Anomaly Warnings",
                            description = "Autonomous spending trajectory analysis and runaway subscription alerts.",
                            checked = aiConsent,
                            enabled = true,
                            onCheckedChange = { aiConsent = it }
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = {
                                viewModel.updatePrivacyConsent(
                                    analytics = analyticsConsent,
                                    marketing = marketingConsent,
                                    location = locationConsent,
                                    ai = aiConsent
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = BerryPrimary),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("btn_save_consents")
                        ) {
                            Text("Save Consent Preferences", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // 3. Section 11 Right to Access & Data Portability
            item {
                Text(
                    text = "DATA PORTABILITY (DPDP SEC. 11)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp,
                    color = SpaceTextSecondary
                )
                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = SpaceCard),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.DownloadForOffline,
                                contentDescription = null,
                                tint = CyanAccent,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Machine-Readable Data Export",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = SpaceTextPrimary
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Download a complete cryptographic JSON archive of your personal profile, transactions, budgets, reminders, trips, and consent states.",
                            fontSize = 12.sp,
                            color = SpaceTextSecondary
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedButton(
                            onClick = {
                                coroutineScope.launch {
                                    val json = viewModel.exportUserDataJson()
                                    exportJsonContent = json
                                    showExportDialog = true
                                }
                            },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("btn_export_data")
                        ) {
                            Icon(Icons.Default.Code, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Generate JSON Archive", fontWeight = FontWeight.SemiBold, color = BerryPrimary)
                        }
                    }
                }
            }

            // 4. Privacy Audit Trail (Immutable Log)
            item {
                Text(
                    text = "PRIVACY AUDIT TRAIL (${auditLogs.size})",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp,
                    color = SpaceTextSecondary
                )
            }

            if (auditLogs.isEmpty()) {
                item {
                    Text(
                        text = "No audit log entries recorded yet.",
                        fontSize = 13.sp,
                        color = SpaceTextSecondary
                    )
                }
            } else {
                items(auditLogs.take(10), key = { it.id }) { log ->
                    AuditLogRow(item = log)
                }
            }

            // 5. Section 12 Right to Erasure / Danger Zone
            item {
                Text(
                    text = "DANGER ZONE (DPDP SEC. 12)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp,
                    color = RoseAccent
                )
                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, RoseAccent.copy(alpha = 0.4f), RoundedCornerShape(18.dp)),
                    colors = CardDefaults.cardColors(containerColor = SpaceCard),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.WarningAmber,
                                contentDescription = null,
                                tint = RoseAccent,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Irreversible Account Erasure",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = RoseAccent
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Pursuant to DPDP Act Section 12, this will immediately and irrevocably destroy all transaction ledgers, active trips, budgets, and personal identifiers.",
                            fontSize = 12.sp,
                            color = SpaceTextSecondary
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = {
                                deleteConfirmationText = ""
                                showDeleteConfirmDialog = true
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = RoseAccent),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("btn_request_erasure")
                        ) {
                            Icon(Icons.Default.DeleteForever, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Erase All Data & Anonymize", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // Guardian Request Dialog
    if (showGuardianDialog) {
        var gName by remember { mutableStateOf(privacySettings?.guardianName ?: "") }
        var gEmail by remember { mutableStateOf(privacySettings?.guardianEmail ?: "") }

        AlertDialog(
            onDismissRequest = { showGuardianDialog = false },
            title = {
                Text("Verifiable Parental Consent", fontWeight = FontWeight.Bold, fontFamily = FontFamily.Serif)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Enter your parent or legal guardian's details to transmit an authorization token per DPDP Act Section 9.",
                        fontSize = 12.sp,
                        color = SpaceTextSecondary
                    )
                    OutlinedTextField(
                        value = gName,
                        onValueChange = { gName = it },
                        label = { Text("Parent / Guardian Full Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = gEmail,
                        onValueChange = { gEmail = it },
                        label = { Text("Guardian Email Address") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (gName.isNotBlank() && gEmail.isNotBlank()) {
                            viewModel.requestGuardianConsent(gName, gEmail)
                            showGuardianDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BerryPrimary)
                ) {
                    Text("Send Consent Token")
                }
            },
            dismissButton = {
                TextButton(onClick = { showGuardianDialog = false }) {
                    Text("Cancel", color = SpaceTextSecondary)
                }
            },
            containerColor = SpaceCard
        )
    }

    // Export JSON Dialog
    if (showExportDialog) {
        AlertDialog(
            onDismissRequest = { showExportDialog = false },
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Personal Data Export", fontWeight = FontWeight.Bold, fontFamily = FontFamily.Serif)
                    IconButton(onClick = {
                        clipboardManager.setText(AnnotatedString(exportJsonContent))
                    }) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "Copy JSON", tint = BerryPrimary)
                    }
                }
            },
            text = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .background(SpaceBlack, RoundedCornerShape(8.dp))
                        .padding(10.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = exportJsonContent,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        color = SpaceTextPrimary
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { showExportDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = BerryPrimary)
                ) {
                    Text("Close")
                }
            },
            containerColor = SpaceCard
        )
    }

    // Danger Zone Confirmation Dialog
    if (showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            title = {
                Text("Confirm Irreversible Erasure", fontWeight = FontWeight.Bold, color = RoseAccent)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "To confirm that you wish to erase all financial history and anonymize your account, type \"DELETE MY DATA\" below:",
                        fontSize = 12.sp,
                        color = SpaceTextSecondary
                    )
                    OutlinedTextField(
                        value = deleteConfirmationText,
                        onValueChange = { deleteConfirmationText = it },
                        placeholder = { Text("DELETE MY DATA") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (deleteConfirmationText.trim() == "DELETE MY DATA") {
                            viewModel.eraseUserData()
                            showDeleteConfirmDialog = false
                            navController.navigate("home") {
                                popUpTo("home") { inclusive = true }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RoseAccent),
                    enabled = deleteConfirmationText.trim() == "DELETE MY DATA"
                ) {
                    Text("Confirm Erasure")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmDialog = false }) {
                    Text("Cancel", color = SpaceTextSecondary)
                }
            },
            containerColor = SpaceCard
        )
    }
}

@Composable
fun ConsentRow(
    title: String,
    description: String,
    checked: Boolean,
    enabled: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = SpaceTextPrimary
            )
            Text(
                text = description,
                fontSize = 11.sp,
                color = SpaceTextSecondary,
                lineHeight = 15.sp
            )
        }
        Switch(
            checked = checked,
            enabled = enabled,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = BerryPrimary,
                uncheckedTrackColor = SpaceCardAlt
            )
        )
    }
}

@Composable
fun AuditLogRow(item: PrivacyAuditLogItem) {
    val dateStr = remember(item.timestamp) {
        val sdf = SimpleDateFormat("dd MMM, HH:mm", Locale.US)
        sdf.format(Date(item.timestamp))
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SpaceCard),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(SoftBlushBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = null,
                    tint = BerryPrimary,
                    modifier = Modifier.size(14.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.eventType,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = SpaceTextPrimary
                )
                Text(
                    text = item.details,
                    fontSize = 10.sp,
                    color = SpaceTextSecondary,
                    maxLines = 2
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = dateStr,
                fontSize = 9.sp,
                color = SpaceTextSecondary
            )
        }
    }
}
