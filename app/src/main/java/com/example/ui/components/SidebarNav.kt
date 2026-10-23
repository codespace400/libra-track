package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.LibrarianUser
import com.example.model.NavDestination
import com.example.ui.theme.AccentGold
import com.example.ui.theme.OverdueRed
import com.example.ui.theme.PrimaryNavy
import com.example.ui.theme.PrimaryNavyContainer
import com.example.ui.theme.PrimaryNavyDark
import com.example.ui.theme.SurfaceSlateBorder
import com.example.ui.theme.TextPrimarySlate
import com.example.ui.theme.TextSecondarySlate

@Composable
fun SidebarNav(
    currentDestination: NavDestination,
    onNavigate: (NavDestination) -> Unit,
    overdueCount: Int,
    librarian: LibrarianUser,
    onLogout: () -> Unit,
    onQuickAddBook: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .width(260.dp)
            .fillMaxHeight(),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceSlateBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .weight(1f, fill = false)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header Logo
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(PrimaryNavy),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.libratrack_icon_1792763655416),
                            contentDescription = "LibraTrack Crest",
                            modifier = Modifier.size(44.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "LibraTrack",
                            color = PrimaryNavy,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = (-0.5).sp
                        )
                        Text(
                            text = "ADMIN PORTAL",
                            color = AccentGold,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                }

                HorizontalDivider(color = SurfaceSlateBorder, thickness = 1.dp)
                Spacer(modifier = Modifier.height(16.dp))

                // Primary Action Button
                Button(
                    onClick = onQuickAddBook,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("sidebar_add_book_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "New Book",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "MAIN MENU",
                    color = Color(0xFF94A3B8),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                )

                // Nav Items
                SidebarNavItem(
                    label = "Dashboard",
                    icon = Icons.Default.Dashboard,
                    isSelected = currentDestination == NavDestination.DASHBOARD,
                    onClick = { onNavigate(NavDestination.DASHBOARD) }
                )

                SidebarNavItem(
                    label = "Book Catalog",
                    icon = Icons.AutoMirrored.Filled.MenuBook,
                    isSelected = currentDestination == NavDestination.BOOKS || currentDestination == NavDestination.BOOK_DETAIL || currentDestination == NavDestination.ADD_EDIT_BOOK,
                    onClick = { onNavigate(NavDestination.BOOKS) }
                )

                SidebarNavItem(
                    label = "Students",
                    icon = Icons.Default.People,
                    isSelected = currentDestination == NavDestination.STUDENTS,
                    onClick = { onNavigate(NavDestination.STUDENTS) }
                )

                SidebarNavItem(
                    label = "Borrow / Return",
                    icon = Icons.Default.SwapHoriz,
                    isSelected = currentDestination == NavDestination.BORROW_RETURN,
                    onClick = { onNavigate(NavDestination.BORROW_RETURN) }
                )

                SidebarNavItem(
                    label = "Overdue List",
                    icon = Icons.Default.WarningAmber,
                    isSelected = currentDestination == NavDestination.OVERDUE,
                    badgeCount = overdueCount,
                    isAlert = overdueCount > 0,
                    onClick = { onNavigate(NavDestination.OVERDUE) }
                )

                SidebarNavItem(
                    label = "Reports & Analytics",
                    icon = Icons.Default.BarChart,
                    isSelected = currentDestination == NavDestination.REPORTS,
                    onClick = { onNavigate(NavDestination.REPORTS) }
                )
            }

            // Bottom Profile & Logout
            Column(modifier = Modifier.fillMaxWidth()) {
                HorizontalDivider(color = SurfaceSlateBorder, thickness = 1.dp)
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFF8FAFC))
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(PrimaryNavyContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "SJ",
                            color = PrimaryNavy,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = librarian.name,
                            color = TextPrimarySlate,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = librarian.title,
                            color = TextSecondarySlate,
                            fontSize = 10.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onLogout() }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .testTag("sidebar_logout_btn"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = "Log Out",
                        tint = Color(0xFFEF4444),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Sign Out",
                        color = Color(0xFFEF4444),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
fun SidebarNavItem(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    badgeCount: Int? = null,
    isAlert: Boolean = false
) {
    val bgColor = if (isSelected) PrimaryNavyContainer else Color.Transparent
    val contentColor = if (isSelected) PrimaryNavy else TextSecondarySlate
    val iconColor = if (isSelected) PrimaryNavy else Color(0xFF64748B)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 10.dp)
            .testTag("nav_${label.lowercase().replace(" ", "_").replace("/", "")}"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = iconColor,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label,
            color = contentColor,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        if (badgeCount != null && badgeCount > 0) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isAlert) OverdueRed else PrimaryNavy)
                    .padding(horizontal = 7.dp, vertical = 2.dp)
            ) {
                Text(
                    text = badgeCount.toString(),
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
