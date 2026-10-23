package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentLate
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Loan
import com.example.model.LoanStatus
import com.example.ui.components.StatCard
import com.example.ui.theme.AccentGold
import com.example.ui.theme.AccentGoldContainer
import com.example.ui.theme.OverdueRed
import com.example.ui.theme.OverdueRedContainer
import com.example.ui.theme.PrimaryNavy
import com.example.ui.theme.PrimaryNavyContainer
import com.example.ui.theme.SurfaceSlateBorder
import com.example.ui.theme.TextPrimarySlate
import com.example.ui.theme.TextSecondarySlate
import java.util.Locale

@Composable
fun OverdueScreen(
    loans: List<Loan>,
    onCollectFine: (loanId: String) -> Unit,
    onSendReminder: (loanId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }

    val overdueLoans by remember(loans, searchQuery) {
        derivedStateOf {
            loans.filter { it.status == LoanStatus.OVERDUE }
                .filter {
                    searchQuery.isBlank() ||
                            it.studentName.contains(searchQuery, true) ||
                            it.bookTitle.contains(searchQuery, true) ||
                            it.studentNumber.contains(searchQuery, true)
                }
                .sortedByDescending { it.daysOverdue }
        }
    }

    val totalOverdueFines = remember(loans) {
        loans.filter { it.status == LoanStatus.OVERDUE }.sumOf { it.fineAmount }
    }

    val maxOverdueDays = remember(loans) {
        loans.filter { it.status == LoanStatus.OVERDUE }.maxOfOrNull { it.daysOverdue } ?: 0
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Delinquent & Overdue Loans",
                    color = OverdueRed,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Track overdue materials, assess late penalties ($0.50/day standard rate), and issue recall alerts",
                    color = TextSecondarySlate,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Summary Metric Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatCard(
                title = "Total Overdue Loans",
                value = "${overdueLoans.size}",
                subtitle = "Active circulation violations",
                icon = Icons.Default.Warning,
                iconTint = OverdueRed,
                iconBgColor = OverdueRedContainer,
                isUrgent = true,
                modifier = Modifier.weight(1f)
            )

            StatCard(
                title = "Accrued Late Penalties",
                value = "$${String.format(Locale.US, "%.2f", totalOverdueFines)}",
                subtitle = "Calculated at $0.50/day overdue rate",
                icon = Icons.Default.AttachMoney,
                iconTint = AccentGold,
                iconBgColor = AccentGoldContainer,
                modifier = Modifier.weight(1f)
            )

            StatCard(
                title = "Maximum Days Overdue",
                value = "$maxOverdueDays Days",
                subtitle = "Urgent recall required",
                icon = Icons.Default.AssignmentLate,
                iconTint = PrimaryNavy,
                iconBgColor = PrimaryNavyContainer,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Search Filter
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search overdue borrower by name, matric ID, or title...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = OverdueRed) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear")
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("overdue_search_input"),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = OverdueRed,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Table
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = androidx.compose.ui.graphics.SolidColor(OverdueRed.copy(alpha = 0.25f))
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            val horizontalScrollState = rememberScrollState()

            Column(modifier = Modifier.fillMaxSize()) {
                // Table Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFFEF2F2))
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .horizontalScroll(horizontalScrollState)
                ) {
                    Row(
                        modifier = Modifier.width(960.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "STUDENT BORROWER",
                            color = Color(0xFF991B1B),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.width(220.dp)
                        )
                        Text(
                            text = "OVERDUE BOOK",
                            color = Color(0xFF991B1B),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.width(260.dp)
                        )
                        Text(
                            text = "ORIGINAL DUE DATE",
                            color = Color(0xFF991B1B),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.width(140.dp)
                        )
                        Text(
                            text = "DAYS OVERDUE",
                            color = Color(0xFF991B1B),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.width(130.dp)
                        )
                        Text(
                            text = "FINE ACCUMULATED",
                            color = Color(0xFF991B1B),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.width(120.dp)
                        )
                        Text(
                            text = "RECALL ACTIONS",
                            color = Color(0xFF991B1B),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.width(90.dp)
                        )
                    }
                }

                HorizontalDivider(color = Color(0xFFFCA5A5))

                if (overdueLoans.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color(0xFF16A34A),
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Zero Overdue Loans!",
                                color = TextPrimarySlate,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "All student patrons are in good standing with active loans.",
                                color = TextSecondarySlate,
                                fontSize = 12.sp
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .horizontalScroll(horizontalScrollState)
                    ) {
                        items(overdueLoans, key = { it.id }) { loan ->
                            Row(
                                modifier = Modifier
                                    .width(960.dp)
                                    .background(Color(0xFFFFFDFD))
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Student
                                Column(modifier = Modifier.width(220.dp)) {
                                    Text(
                                        text = loan.studentName,
                                        color = TextPrimarySlate,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = loan.studentNumber,
                                        color = TextSecondarySlate,
                                        fontSize = 11.sp,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }

                                // Book Title
                                Column(modifier = Modifier.width(260.dp)) {
                                    Text(
                                        text = loan.bookTitle,
                                        color = TextPrimarySlate,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        maxLines = 1
                                    )
                                    Text(
                                        text = loan.bookAuthor,
                                        color = TextSecondarySlate,
                                        fontSize = 11.sp,
                                        maxLines = 1
                                    )
                                }

                                // Original Due Date
                                Text(
                                    text = loan.dueDate,
                                    color = TextSecondarySlate,
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily.Monospace,
                                    modifier = Modifier.width(140.dp)
                                )

                                // Days Overdue Highlighted Pill
                                Box(modifier = Modifier.width(130.dp)) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(OverdueRedContainer)
                                            .border(1.dp, OverdueRed.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                                            .padding(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = "${loan.daysOverdue} Days Late",
                                            color = OverdueRed,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    }
                                }

                                // Fine Amount Highlighted Red
                                Text(
                                    text = "$${String.format(Locale.US, "%.2f", loan.fineAmount)}",
                                    color = OverdueRed,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    modifier = Modifier.width(120.dp)
                                )

                                // Actions
                                Row(
                                    modifier = Modifier.width(90.dp),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    IconButton(
                                        onClick = { onCollectFine(loan.id) },
                                        modifier = Modifier
                                            .size(32.dp)
                                            .testTag("collect_fine_btn_${loan.id}")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Paid,
                                            contentDescription = "Collect fine & return",
                                            tint = Color(0xFF16A34A),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }

                                    IconButton(
                                        onClick = { onSendReminder(loan.id) },
                                        modifier = Modifier
                                            .size(32.dp)
                                            .testTag("send_reminder_btn_${loan.id}")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Email,
                                            contentDescription = "Send notification",
                                            tint = PrimaryNavy,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                            HorizontalDivider(color = Color(0xFFFEE2E2))
                        }
                    }
                }
            }
        }
    }
}
