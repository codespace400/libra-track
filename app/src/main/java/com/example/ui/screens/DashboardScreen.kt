package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ActivityLog
import com.example.model.ActivityType
import com.example.model.Book
import com.example.model.Loan
import com.example.model.LoanStatus
import com.example.model.NavDestination
import com.example.model.Student
import com.example.ui.components.BookCoverArt
import com.example.ui.components.BookStatusBadge
import com.example.ui.components.StatCard
import com.example.ui.theme.AccentGold
import com.example.ui.theme.AccentGoldContainer
import com.example.ui.theme.OverdueRed
import com.example.ui.theme.OverdueRedContainer
import com.example.ui.theme.PrimaryNavy
import com.example.ui.theme.PrimaryNavyContainer
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.SuccessGreenContainer
import com.example.ui.theme.SurfaceSlateBorder
import com.example.ui.theme.TextPrimarySlate
import com.example.ui.theme.TextSecondarySlate
import java.util.Locale

@Composable
fun DashboardScreen(
    books: List<Book>,
    students: List<Student>,
    loans: List<Loan>,
    activities: List<ActivityLog>,
    onNavigate: (NavDestination) -> Unit,
    onBookClick: (Book) -> Unit,
    modifier: Modifier = Modifier
) {
    val totalCopies = books.sumOf { it.totalCopies }
    val availableCopies = books.sumOf { it.availableCopies }
    val activeLoans = loans.filter { it.status == LoanStatus.ACTIVE || it.status == LoanStatus.OVERDUE }
    val overdueLoans = loans.filter { it.status == LoanStatus.OVERDUE }
    val totalFines = overdueLoans.sumOf { it.fineAmount }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Top Banner / Welcome
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = PrimaryNavy),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Central Library Operations Overview",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Real-time loan transactions, overdue fines tracking, and inventory status.",
                            color = Color(0xFFDBEAFE),
                            fontSize = 13.sp
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { onNavigate(NavDestination.BORROW_RETURN) },
                            colors = ButtonDefaults.buttonColors(containerColor = AccentGold),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("dashboard_quick_issue_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.SwapHoriz,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Issue Loan", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Stat Cards 4-grid / responsive row
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    StatCard(
                        title = "Total Books",
                        value = totalCopies.toString(),
                        subtitle = "$availableCopies available in stacks (${books.size} titles)",
                        icon = Icons.AutoMirrored.Filled.MenuBook,
                        iconTint = PrimaryNavy,
                        iconBgColor = PrimaryNavyContainer,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(NavDestination.BOOKS) }
                    )

                    StatCard(
                        title = "Books Borrowed",
                        value = activeLoans.size.toString(),
                        subtitle = "${activeLoans.filter { it.status == LoanStatus.ACTIVE }.size} on active student loans",
                        icon = Icons.Default.SwapHoriz,
                        iconTint = AccentGold,
                        iconBgColor = AccentGoldContainer,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(NavDestination.BORROW_RETURN) }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    StatCard(
                        title = "Overdue Books",
                        value = overdueLoans.size.toString(),
                        subtitle = "$${String.format(Locale.US, "%.2f", totalFines)} outstanding fines",
                        icon = Icons.Default.Warning,
                        iconTint = OverdueRed,
                        iconBgColor = OverdueRedContainer,
                        isUrgent = overdueLoans.isNotEmpty(),
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(NavDestination.OVERDUE) }
                    )

                    StatCard(
                        title = "Total Students",
                        value = students.size.toString(),
                        subtitle = "${students.count { it.activeLoansCount > 0 }} with active checkouts",
                        icon = Icons.Default.People,
                        iconTint = Color(0xFF0F766E),
                        iconBgColor = Color(0xFFCCFBF1),
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(NavDestination.STUDENTS) }
                    )
                }
            }
        }

        // Dual Section: Recent Activity Feed & Featured Catalog
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Recent Activity Feed
                Card(
                    modifier = Modifier.weight(1.3f),
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
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Recent Activity Feed",
                                color = TextPrimarySlate,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Live Audit",
                                color = TextSecondarySlate,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = SurfaceSlateBorder)

                        activities.take(5).forEach { activity ->
                            ActivityRow(activity)
                            HorizontalDivider(color = Color(0xFFF1F5F9))
                        }
                    }
                }

                // Quick Popular Catalog
                Card(
                    modifier = Modifier.weight(1.1f),
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
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Popular Stacks",
                                color = TextPrimarySlate,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "View All",
                                color = PrimaryNavy,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier
                                    .clickable { onNavigate(NavDestination.BOOKS) }
                                    .testTag("dashboard_view_all_books")
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = SurfaceSlateBorder)

                        books.take(4).forEach { book ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onBookClick(book) }
                                    .padding(vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                BookCoverArt(
                                    book = book,
                                    width = 44.dp,
                                    height = 60.dp
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = book.title,
                                        color = TextPrimarySlate,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        maxLines = 1
                                    )
                                    Text(
                                        text = "${book.author} • ${book.genre}",
                                        color = TextSecondarySlate,
                                        fontSize = 11.sp,
                                        maxLines = 1
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "${book.availableCopies} of ${book.totalCopies} available",
                                        color = if (book.availableCopies > 0) Color(0xFF16A34A) else Color(0xFFDC2626),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = "Details",
                                    tint = Color(0xFF94A3B8),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            HorizontalDivider(color = Color(0xFFF1F5F9))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ActivityRow(activity: ActivityLog) {
    val (icon, iconColor, iconBg) = when (activity.type) {
        ActivityType.BORROW -> Triple(Icons.Default.SwapHoriz, AccentGold, AccentGoldContainer)
        ActivityType.RETURN -> Triple(Icons.Default.AssignmentTurnedIn, SuccessGreen, SuccessGreenContainer)
        ActivityType.ADD_BOOK -> Triple(Icons.Default.Add, PrimaryNavy, PrimaryNavyContainer)
        ActivityType.OVERDUE_ALERT -> Triple(Icons.Default.NotificationsActive, OverdueRed, OverdueRedContainer)
        ActivityType.FINE_COLLECTED -> Triple(Icons.Default.Paid, Color(0xFF0F766E), Color(0xFFCCFBF1))
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(16.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = activity.title,
                color = TextPrimarySlate,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = activity.subtitle,
                color = TextSecondarySlate,
                fontSize = 11.sp
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = activity.timestamp,
            color = Color(0xFF94A3B8),
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
