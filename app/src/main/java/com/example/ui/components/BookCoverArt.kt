package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Book

val coverPalettes = listOf(
    Pair(Color(0xFF1E3A8A), Color(0xFF172554)), // Deep Navy
    Pair(Color(0xFF0F766E), Color(0xFF134E4A)), // Deep Teal
    Pair(Color(0xFF7C2D12), Color(0xFF431407)), // Deep Mahogany
    Pair(Color(0xFF4C1D95), Color(0xFF2E1065)), // Royal Purple
    Pair(Color(0xFF9A3412), Color(0xFF7C2D12)), // Amber Rust
    Pair(Color(0xFF1E293B), Color(0xFF0F172A))  // Dark Slate
)

@Composable
fun BookCoverArt(
    book: Book,
    modifier: Modifier = Modifier,
    height: Dp = 120.dp,
    width: Dp = 85.dp
) {
    val palette = coverPalettes.getOrElse(book.coverGradientIndex % coverPalettes.size) { coverPalettes[0] }

    Box(
        modifier = modifier
            .size(width = width, height = height)
            .shadow(4.dp, RoundedCornerShape(topEnd = 6.dp, bottomEnd = 6.dp))
            .clip(RoundedCornerShape(topStart = 2.dp, bottomStart = 2.dp, topEnd = 6.dp, bottomEnd = 6.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(
                        Color(0xFF0F172A).copy(alpha = 0.8f),
                        palette.second,
                        palette.first
                    )
                )
            )
    ) {
        // Spine groove line
        Box(
            modifier = Modifier
                .width(4.dp)
                .fillMaxHeight()
                .background(Color.Black.copy(alpha = 0.25f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 8.dp, end = 6.dp, top = 8.dp, bottom = 6.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = book.genre.take(12).uppercase(),
                    color = Color(0xFFF59E0B),
                    fontSize = 7.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp,
                    maxLines = 1
                )
                Icon(
                    imageVector = Icons.Default.Bookmark,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.6f),
                    modifier = Modifier.size(10.dp)
                )
            }

            Column {
                Text(
                    text = book.title,
                    color = Color.White,
                    fontSize = if (height > 140.dp) 12.sp else 9.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    lineHeight = if (height > 140.dp) 14.sp else 11.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = book.author,
                    color = Color(0xFFCBD5E1),
                    fontSize = if (height > 140.dp) 9.sp else 7.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Icon(
                    imageVector = Icons.Default.AutoStories,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.35f),
                    modifier = Modifier.size(if (height > 140.dp) 16.dp else 12.dp)
                )
            }
        }
    }
}
