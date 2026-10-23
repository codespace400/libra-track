package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BookStatus
import com.example.model.LoanStatus
import com.example.ui.theme.AccentGold
import com.example.ui.theme.AccentGoldContainer
import com.example.ui.theme.OnAccentGoldContainer
import com.example.ui.theme.OnOverdueRed
import com.example.ui.theme.OnSuccessGreen
import com.example.ui.theme.OverdueRed
import com.example.ui.theme.OverdueRedContainer
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.SuccessGreenContainer

@Composable
fun BookStatusBadge(status: BookStatus, modifier: Modifier = Modifier) {
    val (bgColor, textColor, borderColor, label) = when (status) {
        BookStatus.AVAILABLE -> Quadruple(
            SuccessGreenContainer,
            OnSuccessGreen,
            SuccessGreen.copy(alpha = 0.3f),
            "Available"
        )
        BookStatus.LOW_STOCK -> Quadruple(
            AccentGoldContainer,
            OnAccentGoldContainer,
            AccentGold.copy(alpha = 0.4f),
            "Low Stock"
        )
        BookStatus.OUT_OF_STOCK -> Quadruple(
            OverdueRedContainer,
            OnOverdueRed,
            OverdueRed.copy(alpha = 0.4f),
            "Borrowed Out"
        )
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = label,
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun LoanStatusBadge(status: LoanStatus, daysOverdue: Int = 0, modifier: Modifier = Modifier) {
    val (bgColor, textColor, borderColor, label) = when (status) {
        LoanStatus.ACTIVE -> Quadruple(
            Color(0xFFDBEAFE),
            Color(0xFF1E3A8A),
            Color(0xFF93C5FD),
            "Active Loan"
        )
        LoanStatus.OVERDUE -> Quadruple(
            OverdueRedContainer,
            OnOverdueRed,
            OverdueRed.copy(alpha = 0.4f),
            if (daysOverdue > 0) "$daysOverdue Days Overdue" else "Overdue"
        )
        LoanStatus.RETURNED -> Quadruple(
            SuccessGreenContainer,
            OnSuccessGreen,
            SuccessGreen.copy(alpha = 0.3f),
            "Returned"
        )
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = label,
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
