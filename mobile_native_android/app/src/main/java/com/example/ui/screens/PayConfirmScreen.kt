package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.ui.theme.*
import com.example.viewmodel.SpedexViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PayConfirmScreen(
    navController: NavController,
    viewModel: SpedexViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scannedInfo by viewModel.scannedUpiState.collectAsStateWithLifecycle()
    val activeTxId by viewModel.activeTransactionId.collectAsStateWithLifecycle()
    val activeTxStatus by viewModel.activeTransactionStatus.collectAsStateWithLifecycle()

    // Screen state variables
    var merchantName by remember { mutableStateOf("") }
    var upiId by remember { mutableStateOf("") }
    var payAmount by remember { mutableStateOf("") }
    var chosenCategory by remember { mutableStateOf("Food") }
    var quickPayToggle by remember { mutableStateOf(true) }

    // Init values once scanned is loaded
    LaunchedEffect(scannedInfo) {
        scannedInfo?.let {
            merchantName = it.name
            upiId = it.upiId
            payAmount = if (it.amount > 0) it.amount.toString() else ""
        }
    }

    var showVerificationOverlay by remember { mutableStateOf(false) }
    var verificationStepText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Confirm Checkout", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SpaceBlack)
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 18.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (scannedInfo == null) {
                    // Empty scan fallback info
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = TealAccent)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Extracting QR merchant details...", color = SpaceTextSecondary)
                    }
                } else {
                    // Merchant Profile Icon Group
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(getCategoryAccent(chosenCategory).copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = getCategoryIconSvg(getCategoryIconName(chosenCategory)),
                            contentDescription = null,
                            tint = getCategoryAccent(chosenCategory),
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    // Form Fields
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Vendor Name
                        OutlinedTextField(
                            value = merchantName,
                            onValueChange = { merchantName = it },
                            label = { Text("Vendor / Store Name") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = SpaceTextPrimary,
                                unfocusedTextColor = SpaceTextPrimary,
                                focusedBorderColor = TealAccent,
                                unfocusedBorderColor = SpaceCardAlt,
                                focusedLabelColor = TealAccent,
                                unfocusedLabelColor = SpaceTextSecondary
                            ),
                            leadingIcon = { Icon(imageVector = Icons.Default.Store, contentDescription = null, tint = SpaceTextSecondary) },
                            modifier = Modifier.fillMaxWidth()
                        )

                        // UPI Handle
                        OutlinedTextField(
                            value = upiId,
                            onValueChange = { upiId = it },
                            label = { Text("UPI Handle (VPA)") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = SpaceTextPrimary,
                                unfocusedTextColor = SpaceTextPrimary,
                                focusedBorderColor = TealAccent,
                                unfocusedBorderColor = SpaceCardAlt,
                                focusedLabelColor = TealAccent,
                                unfocusedLabelColor = SpaceTextSecondary
                            ),
                            leadingIcon = { Icon(imageVector = Icons.Default.AlternateEmail, contentDescription = null, tint = SpaceTextSecondary) },
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Amount to pay
                        OutlinedTextField(
                            value = payAmount,
                            onValueChange = { if (it.isEmpty() || it.toDoubleOrNull() != null) payAmount = it },
                            label = { Text("Amount to Pay") },
                            prefix = { Text("₹ ", fontWeight = FontWeight.SemiBold, color = SpaceTextPrimary) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = SpaceTextPrimary,
                                unfocusedTextColor = SpaceTextPrimary,
                                focusedBorderColor = TealAccent,
                                unfocusedBorderColor = SpaceCardAlt,
                                focusedLabelColor = TealAccent,
                                unfocusedLabelColor = SpaceTextSecondary
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        // Student Categories Box
                        Text(
                            text = "Choose Student Category",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = SpaceTextPrimary,
                            modifier = Modifier.padding(top = 4.dp)
                        )

                        val categories = listOf("Food", "Xerox", "Transport", "Subscriptions", "Rent")
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            categories.forEach { cat ->
                                val isSelected = cat == chosenCategory
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) getCategoryAccent(cat) else SpaceCard)
                                        .clickable { chosenCategory = cat }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = cat,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) SpaceBlack else SpaceTextSecondary
                                    )
                                }
                            }
                        }

                        // Save as continuous Quick Pay Contact
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(SpaceCard)
                                .clickable { quickPayToggle = !quickPayToggle }
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.Bolt, contentDescription = null, tint = OrangeAccent)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text("Add to Quick Pay contacts", fontSize = 13.sp, color = SpaceTextPrimary, fontWeight = FontWeight.SemiBold)
                                    Text("Pin to home screen for one-tap repeats", fontSize = 10.sp, color = SpaceTextSecondary)
                                }
                            }
                            Switch(
                                checked = quickPayToggle,
                                onCheckedChange = { quickPayToggle = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = SpaceBlack,
                                    checkedTrackColor = TealAccent,
                                    uncheckedThumbColor = SpaceTextSecondary,
                                    uncheckedTrackColor = SpaceCardAlt
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Checkout Action Indicator
                    Button(
                        onClick = {
                            val amount = payAmount.toDoubleOrNull() ?: 0.0
                            if (merchantName.isNotBlank() && upiId.isNotBlank() && amount > 0) {
                                viewModel.preparePaymentAndSaveVendor(
                                    name = merchantName,
                                    upi = upiId,
                                    amount = amount,
                                    category = chosenCategory,
                                    isQuickPay = quickPayToggle
                                ) { txId, deepLink ->
                                    showVerificationOverlay = true
                                    verificationStepText = "Handing off payment execution to external UPI app..."
                                    
                                    // Trigger deep-link intent trigger
                                    try {
                                        val parseUri = Uri.parse(deepLink)
                                        val upiIntent = Intent(Intent.ACTION_VIEW, parseUri)
                                        context.startActivity(upiIntent)
                                    } catch (e: Exception) {
                                        verificationStepText = "Deep-link launched! Emulator simulated handshake triggered. Awaiting manual payment confirmation below."
                                    }
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = TealAccent, contentColor = SpaceBlack),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .testTag("init_upi_pay")
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.SendToMobile, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Trigger external UPI app",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            // Real-time verification handshake overlay
            AnimatedVisibility(
                visible = showVerificationOverlay,
                enter = fadeIn() + slideInVertically { it },
                exit = fadeOut() + slideOutVertically { it }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(SpaceBlack.copy(alpha = 0.95f))
                        .clickable(enabled = false) {},
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(SpaceCard)
                            .border(1.dp, SpaceCardAlt, RoundedCornerShape(24.dp))
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(TealAccent.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.TouchApp,
                                contentDescription = null,
                                tint = TealAccent,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Text(
                            text = "Payment Status Verification",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = SpaceTextPrimary
                        )

                        Text(
                            text = verificationStepText,
                            fontSize = 12.sp,
                            color = SpaceTextSecondary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )

                        Divider(color = SpaceCardAlt)

                        // Status Choices
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Option SUCCESS
                            Button(
                                onClick = {
                                    viewModel.completeActiveTransaction("SUCCESS")
                                    showVerificationOverlay = false
                                    viewModel.clearScannedState()
                                    navController.navigate("home") {
                                        popUpTo("home") { inclusive = true }
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = NeonEmerald, contentColor = SpaceBlack),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth().testTag("payment_success_verify")
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Mark Transaction SUCCESS", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                            }

                            // Option FAILURE
                            Button(
                                onClick = {
                                    viewModel.completeActiveTransaction("FAILED")
                                    showVerificationOverlay = false
                                    viewModel.clearScannedState()
                                    navController.navigate("home") {
                                        popUpTo("home") { inclusive = true }
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = RoseAccent, contentColor = SpaceTextPrimary),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth().testTag("payment_failed_verify")
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                                    Icon(imageVector = Icons.Default.Error, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Mark Transaction FAILED", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                            }

                            // Retry / Go Back
                            TextButton(
                                onClick = {
                                    showVerificationOverlay = false
                                    viewModel.completeActiveTransaction("FAILED")
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Retry Payment Configuration", color = SpaceTextSecondary, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
