package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.ui.theme.*
import com.example.viewmodel.SpedexViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScannerScreen(
    navController: NavController,
    viewModel: SpedexViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
    }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    // Dynamic scanning animation for the viewfinder line
    val infiniteTransition = rememberInfiniteTransition(label = "laser")
    val laserOffsetFraction by infiniteTransition.animateFloat(
        initialValue = 0.1f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "laser_y"
    )

    var manualInput by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Scan UPI QR Code", fontWeight = FontWeight.Bold, color = SpaceTextPrimary) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = SpaceTextPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SpaceBlack)
            )
        },
        containerColor = SpaceBlack,
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Lens preview or permission prompt
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(SpaceCard)
                    .border(2.dp, SpaceCardAlt, RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (hasCameraPermission) {
                    // Render CameraX preview safely
                    AndroidView(
                        factory = { ctx ->
                            val previewView = PreviewView(ctx)
                            try {
                                val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                                cameraProviderFuture.addListener({
                                    try {
                                        val cameraProvider = cameraProviderFuture.get()
                                        
                                        // Guard against empty / non-existent camera sensors on emulator
                                        val cameraSelector = if (cameraProvider.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA)) {
                                            CameraSelector.DEFAULT_BACK_CAMERA
                                        } else if (cameraProvider.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA)) {
                                            CameraSelector.DEFAULT_FRONT_CAMERA
                                        } else {
                                            null
                                        }

                                        if (cameraSelector != null) {
                                            val preview = Preview.Builder().build().also {
                                                it.setSurfaceProvider(previewView.surfaceProvider)
                                            }
                                            cameraProvider.unbindAll()
                                            cameraProvider.bindToLifecycle(
                                                lifecycleOwner,
                                                cameraSelector,
                                                preview
                                            )
                                        }
                                    } catch (e: Throwable) {
                                        e.printStackTrace()
                                    }
                                }, ContextCompat.getMainExecutor(ctx))
                            } catch (e: Throwable) {
                                e.printStackTrace()
                            }
                            previewView
                        },
                        modifier = Modifier.fillMaxSize()
                    )

                    // Overlay QR Viewfinder Corners
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val width = size.width
                        val height = size.height
                        val boxSize = 200.dp.toPx()
                        val left = (width - boxSize) / 2
                        val top = (height - boxSize) / 2
                        val right = left + boxSize
                        val bottom = top + boxSize
                        val cornerLen = 24.dp.toPx()
                        val strokeWidth = 3.dp.toPx()

                        // Dark semi-transparent scrim around scanning window
                        drawRect(
                            color = Color.Black.copy(alpha = 0.5f)
                        )

                        // Clear scanning target area
                        drawContext.canvas.save()
                        // Keep a border highlight
                        drawRect(
                            color = TealAccent.copy(alpha = 0.1f),
                            topLeft = Offset(left, top),
                            size = androidx.compose.ui.geometry.Size(boxSize, boxSize)
                        )

                        // Corners: Left-Top
                        drawLine(color = TealAccent, start = Offset(left, top), end = Offset(left + cornerLen, top), strokeWidth = strokeWidth)
                        drawLine(color = TealAccent, start = Offset(left, top), end = Offset(left, top + cornerLen), strokeWidth = strokeWidth)

                        // Corners: Right-Top
                        drawLine(color = TealAccent, start = Offset(right, top), end = Offset(right - cornerLen, top), strokeWidth = strokeWidth)
                        drawLine(color = TealAccent, start = Offset(right, top), end = Offset(right, top + cornerLen), strokeWidth = strokeWidth)

                        // Corners: Left-Bottom
                        drawLine(color = TealAccent, start = Offset(left, bottom), end = Offset(left + cornerLen, bottom), strokeWidth = strokeWidth)
                        drawLine(color = TealAccent, start = Offset(left, bottom), end = Offset(left, bottom - cornerLen), strokeWidth = strokeWidth)

                        // Corners: Right-Bottom
                        drawLine(color = TealAccent, start = Offset(right, bottom), end = Offset(right - cornerLen, bottom), strokeWidth = strokeWidth)
                        drawLine(color = TealAccent, start = Offset(right, bottom), end = Offset(right, bottom - cornerLen), strokeWidth = strokeWidth)

                        // Animated scanning laser line
                        val laserY = top + (boxSize * laserOffsetFraction)
                        drawLine(
                            color = NeonEmerald,
                            start = Offset(left + 8.dp.toPx(), laserY),
                            end = Offset(right - 8.dp.toPx(), laserY),
                            strokeWidth = 2.dp.toPx()
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.QrCode,
                            contentDescription = null,
                            tint = SpaceTextSecondary,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Camera access is useful to scan real vendor barcodes.",
                            color = SpaceTextSecondary,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Emulator Test Presets - INGESTION SANDBOX
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SpaceCard),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp)
                ) {
                    Text(
                        text = "Emulator Ingestion Sandbox",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TealAccent,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = "Real cameras cannot see actual physical QRs in headless VNC. Tap a campus vendor quick-mock code below to inject scanned transaction details instantly:",
                        fontSize = 11.sp,
                        color = SpaceTextSecondary,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    // Column of convenient presets
                    PresetRow(
                        title = "Sai Xerox Split (am=₹15.00)",
                        upi = "upi://pay?pa=saixeroxtpt@okaxis&pn=Sai+Xerox+Shop&am=15.00&cu=INR",
                        onClick = {
                            viewModel.parseUpiString(it)
                            navController.navigate("pay_confirm")
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    PresetRow(
                        title = "Campus Tea Stall (am=₹12.00)",
                        upi = "upi://pay?pa=campus.chai@okicici&pn=Campus+Tea+Stall&am=12.00&cu=INR",
                        onClick = {
                            viewModel.parseUpiString(it)
                            navController.navigate("pay_confirm")
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    PresetRow(
                        title = "Auto Campus Shuttle (am=₹30.00)",
                        upi = "upi://pay?pa=riteshauto@oksbi&pn=Rickshaw+Campus+Shuttle&am=30.00&cu=INR",
                        onClick = {
                            viewModel.parseUpiString(it)
                            navController.navigate("pay_confirm")
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Paste Custom UPI String
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SpaceCard),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.ContentPaste, contentDescription = null, tint = SpaceTextSecondary, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Or Paste UPI Deep Link / Address",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = SpaceTextPrimary
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = manualInput,
                        onValueChange = {
                            manualInput = it
                            errorMessage = ""
                        },
                        placeholder = { Text("upi://pay?pa=example@upi&pn=Vendor...", fontSize = 12.sp, color = SpaceTextSecondary) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = SpaceTextPrimary,
                            unfocusedTextColor = SpaceTextPrimary,
                            focusedBorderColor = TealAccent,
                            unfocusedBorderColor = SpaceCardAlt,
                            cursorColor = TealAccent
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("manual_upi_input"),
                        singleLine = true
                    )

                    if (errorMessage.isNotEmpty()) {
                        Text(
                            text = errorMessage,
                            color = RoseAccent,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(top = 4.dp, start = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            if (manualInput.isBlank()) {
                                errorMessage = "UPI ID or Deep Link is required"
                            } else {
                                val parsed = viewModel.parseUpiString(manualInput)
                                if (parsed != null) {
                                    navController.navigate("pay_confirm")
                                } else {
                                    errorMessage = "Invalid UPI Format. Use 'merchant@upi' or upi://pay link"
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = TealAccent, contentColor = SpaceBlack),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("submit_manual_upi")
                    ) {
                        Text("Ingest Vendor & Proceed", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun PresetRow(
    title: String,
    upi: String,
    onClick: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(SpaceCardAlt)
            .clickable { onClick(upi) }
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = title, fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = SpaceTextPrimary)
                Text(text = upi.substring(0, Math.min(upi.length, 30)) + "...", fontSize = 10.sp, color = SpaceTextSecondary)
            }
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(TealAccent.copy(alpha = 0.15f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(text = "Scan Mock", color = TealAccent, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
