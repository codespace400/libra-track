package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Book
import com.example.model.Loan
import com.example.model.Student
import com.example.ui.theme.AccentGold
import com.example.ui.theme.AccentGoldContainer
import com.example.ui.theme.PrimaryNavy
import com.example.ui.theme.PrimaryNavyContainer
import com.example.ui.theme.PrimaryNavyLight
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.SurfaceSlateBorder
import com.example.ui.theme.TextPrimarySlate
import com.example.ui.theme.TextSecondarySlate

@Composable
fun ReportsScreen(
    books: List<Book>,
    students: List<Student>,
    loans: List<Loan>,
    modifier: Modifier = Modifier
) {
    var showExportDialog by remember { mutableStateOf(false) }
    var exportFormat by remember { mutableStateOf("CSV Summary") }

    val totalCirculation = books.sumOf { it.totalCopies - it.availableCopies } + 14

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        // Top Header & Export
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Circulation Intelligence & Reports",
                    color = TextPrimarySlate,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Borrowing patterns, top circulating titles, and inventory efficiency metrics",
                    color = TextSecondarySlate,
                    fontSize = 12.sp
                )
            }

            Button(
                onClick = { showExportDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("export_reports_btn")
            ) {
                Icon(imageVector = Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Export Report", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // High Level KPI Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ReportKpiCard("94.2%", "On-Time Return Rate", "+2.4% vs last term", PrimaryNavy, Modifier.weight(1f))
            ReportKpiCard("8.6 Days", "Avg Loan Duration", "Standard period: 14 days", AccentGold, Modifier.weight(1f))
            ReportKpiCard("68.4%", "Collection Utilization", "Stacks active in circulation", SuccessGreen, Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Charts Dual Grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Chart 1: Most Borrowed Books
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(SurfaceSlateBorder)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Assessment, contentDescription = null, tint = PrimaryNavy, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Most Borrowed Books",
                                color = TextPrimarySlate,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text("Ranked by Circulations", color = TextSecondarySlate, fontSize = 11.sp)
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = SurfaceSlateBorder)
                    Spacer(modifier = Modifier.height(12.dp))

                    val topBooks = listOf(
                        Triple("Introduction to Algorithms", 28, "Computer Science"),
                        Triple("Clean Architecture", 24, "Computer Science"),
                        Triple("To Kill a Mockingbird", 19, "Literature"),
                        Triple("Design Patterns", 17, "Computer Science"),
                        Triple("A Brief History of Time", 14, "Physics")
                    )

                    topBooks.forEachIndexed { index, (title, count, genre) ->
                        Column(modifier = Modifier.padding(vertical = 6.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "#${index + 1} $title",
                                    color = TextPrimarySlate,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = "$count checkouts",
                                    color = PrimaryNavy,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color(0xFFF1F5F9))
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(fraction = count / 30f)
                                        .height(8.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(if (index == 0) AccentGold else PrimaryNavy)
                                )
                            }
                        }
                    }
                }
            }

            // Chart 2: Borrowing Trends Over Time (Canvas Bar Chart)
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(SurfaceSlateBorder)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.AutoMirrored.Filled.TrendingUp, contentDescription = null, tint = AccentGold, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Monthly Borrowing Trends",
                                color = TextPrimarySlate,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text("+18.4% YoY", color = SuccessGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = SurfaceSlateBorder)
                    Spacer(modifier = Modifier.height(16.dp))

                    val monthlyData = listOf(
                        Pair("May", 42),
                        Pair("Jun", 38),
                        Pair("Jul", 25),
                        Pair("Aug", 31),
                        Pair("Sep", 64),
                        Pair("Oct", 79)
                    )

                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                    ) {
                        val maxVal = 90f
                        val barWidth = 24.dp.toPx()
                        val spacing = (size.width - (monthlyData.size * barWidth)) / (monthlyData.size + 1)

                        // Base line
                        drawLine(
                            color = Color(0xFFE2E8F0),
                            start = Offset(0f, size.height - 24.dp.toPx()),
                            end = Offset(size.width, size.height - 24.dp.toPx()),
                            strokeWidth = 1.dp.toPx()
                        )

                        monthlyData.forEachIndexed { index, (month, value) ->
                            val left = spacing + index * (barWidth + spacing)
                            val barHeight = (value / maxVal) * (size.height - 40.dp.toPx())
                            val top = size.height - 24.dp.toPx() - barHeight

                            val barColor = if (month == "Oct") Color(0xFF1E3A8A) else Color(0xFF93C5FD)

                            drawRoundRect(
                                color = barColor,
                                topLeft = Offset(left, top),
                                size = Size(barWidth, barHeight),
                                cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                            )
                        }
                    }

                    // Month Labels
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        monthlyData.forEach { (month, value) ->
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "$value",
                                    color = if (month == "Oct") PrimaryNavy else TextSecondarySlate,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = month,
                                    color = TextSecondarySlate,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Genre & Department Circulation Breakdown
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = androidx.compose.ui.graphics.SolidColor(SurfaceSlateBorder)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "Circulation by Academic Discipline",
                    color = PrimaryNavy,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    GenreProgressPill("Computer Science", "48%", Color(0xFF1E3A8A), Modifier.weight(1f))
                    GenreProgressPill("Literature", "22%", Color(0xFFD97706), Modifier.weight(1f))
                    GenreProgressPill("Physics & Astronomy", "14%", Color(0xFF0F766E), Modifier.weight(1f))
                    GenreProgressPill("Philosophy & Law", "10%", Color(0xFF7C2D12), Modifier.weight(1f))
                    GenreProgressPill("Medical Sciences", "6%", Color(0xFF6B21A8), Modifier.weight(1f))
                }
            }
        }
    }

    // Export Confirmation Dialog
    if (showExportDialog) {
        AlertDialog(
            onDismissRequest = { showExportDialog = false },
            icon = { Icon(Icons.Default.Download, contentDescription = null, tint = PrimaryNavy) },
            title = {
                Text(
                    text = "Generate Circulation Audit Report",
                    fontWeight = FontWeight.Bold,
                    color = PrimaryNavy,
                    fontSize = 18.sp
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "The report contains full circulation activity, overdue penalty ledgers, and student catalog checkouts for the academic session.",
                        color = TextSecondarySlate,
                        fontSize = 13.sp
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("CSV Summary", "PDF Document", "JSON Ledger").forEach { format ->
                            val isChosen = exportFormat == format
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isChosen) PrimaryNavyContainer else Color(0xFFF1F5F9))
                                    .border(
                                        1.dp,
                                        if (isChosen) PrimaryNavy else SurfaceSlateBorder,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable { exportFormat = format }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = format,
                                    color = if (isChosen) PrimaryNavy else TextPrimarySlate,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFF8FAFC))
                            .border(1.dp, SurfaceSlateBorder, RoundedCornerShape(6.dp))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "Report ID: RPT-2026-10-LT\nIncludes: ${books.size} Books • ${students.size} Students • ${loans.size} Loans",
                            color = TextSecondarySlate,
                            fontSize = 11.sp
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showExportDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy),
                    modifier = Modifier.testTag("confirm_export_btn")
                ) {
                    Text("Download $exportFormat")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showExportDialog = false }) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
fun ReportKpiCard(
    value: String,
    title: String,
    trend: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(SurfaceSlateBorder)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = value,
                color = color,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = title,
                color = TextPrimarySlate,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = trend,
                color = TextSecondarySlate,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
fun GenreProgressPill(
    name: String,
    percentage: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = name, color = TextPrimarySlate, fontSize = 11.sp, fontWeight = FontWeight.Medium, maxLines = 1)
            Text(text = percentage, color = color, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(Color(0xFFF1F5F9))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = percentage.replace("%", "").toFloatOrNull()?.div(100f) ?: 0.2f)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(color)
            )
        }
    }
}
