package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.ui.theme.*
import com.example.viewmodel.SpedexViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(
    navController: NavController,
    viewModel: SpedexViewModel,
    modifier: Modifier = Modifier
) {
    val totalExpense by viewModel.totalExpense.collectAsStateWithLifecycle(initialValue = 0.0)
    val spendByCategory by viewModel.spendByCategory.collectAsStateWithLifecycle(initialValue = emptyMap())
    val weeklyData by viewModel.weeklyExpenseData.collectAsStateWithLifecycle(initialValue = emptyList())
    val insights by viewModel.analyticsInsights.collectAsStateWithLifecycle(initialValue = com.example.viewmodel.AnalyticsInsights("None", "Monday", "0%", 0.5f))

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Student Spending Insights", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SpaceBlack)
            )
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
            // Summary Cards Banner
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = SpaceCard),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(text = "DYNAMIC HOSTEL EXPENSE CALCULATOR", fontSize = 10.sp, fontWeight = FontWeight.Black, color = TealAccent)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "₹%,.2f".format(totalExpense),
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Black,
                            color = SpaceTextPrimary
                        )
                        Text(
                            text = "Total cumulative successful payments across all vendor links this week.",
                            color = SpaceTextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            // Custom Canvas Chart Header
            item {
                Text(
                    text = "Weekly Expenditure Curve",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = SpaceTextPrimary
                )
            }

            // Custom Canvas-drawn Line Chart (Dynamic & Stunning!)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = SpaceCard),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .background(SpaceCardAlt, RoundedCornerShape(12.dp))
                                .padding(vertical = 12.dp, horizontal = 16.dp)
                        ) {
                            if (weeklyData.isEmpty() || weeklyData.all { it.second == 0f }) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(imageVector = Icons.Default.Timeline, contentDescription = null, tint = SpaceCard)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Curve awaits SUCCESS UPI transactions", fontSize = 11.sp, color = SpaceTextSecondary)
                                }
                            } else {
                                // Direct Canvas drawing of the Spline Wave
                                Canvas(modifier = Modifier.fillMaxSize()) {
                                    val width = size.width
                                    val height = size.height
                                    val maxVal = weeklyData.maxOf { it.second }.coerceAtLeast(10f)

                                    val points = weeklyData.mapIndexed { index, pair ->
                                        val x = (width / 6f) * index
                                        val y = height - (pair.second / maxVal) * (height - 24.dp.toPx()) - 8.dp.toPx()
                                        Offset(x, y)
                                    }

                                    // 1. Draw horizontal guidance grid lines
                                    drawLine(color = SpaceCard.copy(alpha = 0.5f), start = Offset(0f, height * 0.25f), end = Offset(width, height * 0.25f))
                                    drawLine(color = SpaceCard.copy(alpha = 0.5f), start = Offset(0f, height * 0.5f), end = Offset(width, height * 0.5f))
                                    drawLine(color = SpaceCard.copy(alpha = 0.5f), start = Offset(0f, height * 0.75f), end = Offset(width, height * 0.75f))

                                    // 2. Draw smooth path line with custom gradient
                                    val splinePath = Path().apply {
                                        points.forEachIndexed { idx, point ->
                                            if (idx == 0) moveTo(point.x, point.y)
                                            else {
                                                val prev = points[idx - 1]
                                                // Cubic bezier for a glorious fluid curve
                                                val controlX = (prev.x + point.x) / 2
                                                cubicTo(controlX, prev.y, controlX, point.y, point.x, point.y)
                                            }
                                        }
                                    }

                                    drawPath(
                                        path = splinePath,
                                        color = TealAccent,
                                        style = Stroke(width = 3.dp.toPx())
                                    )

                                    // 3. Draw gradient area fill under the curve
                                    val fillPath = Path().apply {
                                        addPath(splinePath)
                                        lineTo(points.last().x, height)
                                        lineTo(points.first().x, height)
                                        close()
                                    }

                                    drawPath(
                                        path = fillPath,
                                        brush = Brush.verticalGradient(
                                            colors = listOf(TealAccent.copy(alpha = 0.25f), Color.Transparent)
                                        )
                                    )

                                    // 4. Draw high-highlight dots for each data node
                                    points.forEachIndexed { idx, point ->
                                        drawCircle(color = SpaceBlack, radius = 5.dp.toPx(), center = point)
                                        drawCircle(
                                            color = if (idx == points.lastIndex) NeonEmerald else TealAccent,
                                            radius = 3.5.dp.toPx(),
                                            center = point
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Days Labels Grid
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            val labels = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
                            labels.forEach { dayLabel ->
                                Text(
                                    text = dayLabel,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = SpaceTextSecondary,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }

            // Student Spending Analytics (Busiest, Highest, etc.)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Busiest Day
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = SpaceCard)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Icon(imageVector = Icons.Default.CalendarToday, contentDescription = null, tint = OrangeAccent)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Busiest Day", fontSize = 11.sp, color = SpaceTextSecondary)
                            Text(
                                text = insights.busiestDay,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = SpaceTextPrimary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    // Highest Category Sector
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = SpaceCard)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Icon(imageVector = Icons.Default.Category, contentDescription = null, tint = IndigoAccent)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Highest Sector", fontSize = 11.sp, color = SpaceTextSecondary)
                            Text(
                                text = insights.highestSector,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = SpaceTextPrimary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            // Weekend vs Weekday Ratio split
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = SpaceCard),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Weekday vs Weekend Ratio", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = SpaceTextPrimary)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(TealAccent.copy(alpha = 0.15f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(text = "Weekend ${insights.weekendRatioString}", color = TealAccent, fontSize = 9.sp, fontWeight = FontWeight.Black)
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Draw linear proportion block
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(12.dp)
                                .clip(CircleShape)
                        ) {
                            val weekdayFraction = insights.weekdayRatio
                            val weekendFraction = 1f - weekdayFraction

                            if (weekdayFraction > 0f) {
                                Box(
                                    modifier = Modifier
                                        .weight(weekdayFraction)
                                        .fillMaxHeight()
                                        .background(TealAccent)
                                )
                            }
                            if (weekendFraction > 0f) {
                                Box(
                                    modifier = Modifier
                                        .weight(weekendFraction)
                                        .fillMaxHeight()
                                        .background(OrangeAccent)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(TealAccent))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Weekdays", fontSize = 10.sp, color = SpaceTextSecondary)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(OrangeAccent))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Weekends", fontSize = 10.sp, color = SpaceTextSecondary)
                            }
                        }
                    }
                }
            }

            // Category Breakdown Stats Items
            item {
                Text(
                    text = "Category Breakdown",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = SpaceTextPrimary,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            if (spendByCategory.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No SUCCESS payments found to segment.", color = SpaceTextSecondary, fontSize = 12.sp)
                    }
                }
            } else {
                items(spendByCategory.toList()) { (category, totalAmt) ->
                    CategorySpentRow(category = category, amount = totalAmt, totalSum = totalExpense)
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun CategorySpentRow(category: String, amount: Double, totalSum: Double) {
    val fraction = if (totalSum > 0) (amount / totalSum).coerceIn(0.0, 1.0) else 0.0

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SpaceCard),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(getCategoryAccent(category).copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = getCategoryIconSvg(getCategoryIconName(category)),
                        contentDescription = null,
                        tint = getCategoryAccent(category),
                        modifier = Modifier.size(16.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(text = category, fontWeight = FontWeight.Bold, color = SpaceTextPrimary, fontSize = 13.sp)
                    Text(text = "${((fraction) * 100).toInt()}% share of total spend", fontSize = 10.sp, color = SpaceTextSecondary)
                }
            }

            Text(
                text = "₹%,.2f".format(amount),
                fontWeight = FontWeight.Black,
                color = SpaceTextPrimary,
                fontSize = 14.sp
            )
        }
    }
}
