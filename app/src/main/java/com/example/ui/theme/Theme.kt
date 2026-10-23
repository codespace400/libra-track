package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = PrimaryNavy,
    onPrimary = Color.White,
    primaryContainer = PrimaryNavyContainer,
    onPrimaryContainer = OnPrimaryNavyContainer,
    secondary = AccentGold,
    onSecondary = Color.White,
    secondaryContainer = AccentGoldContainer,
    onSecondaryContainer = OnAccentGoldContainer,
    tertiary = PrimaryNavyLight,
    onTertiary = Color.White,
    background = BackgroundSlate,
    onBackground = TextPrimarySlate,
    surface = SurfaceWhite,
    onSurface = TextPrimarySlate,
    surfaceVariant = SurfaceSlate,
    onSurfaceVariant = TextSecondarySlate,
    outline = SurfaceSlateBorder,
    outlineVariant = SurfaceSlateMuted,
    error = OverdueRed,
    errorContainer = OverdueRedContainer,
    onError = Color.White,
    onErrorContainer = OnOverdueRed
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryNavyLight,
    onPrimary = PrimaryNavyDark,
    primaryContainer = PrimaryNavy,
    onPrimaryContainer = Color.White,
    secondary = AccentGoldLight,
    onSecondary = PrimaryNavyDark,
    secondaryContainer = AccentGoldDark,
    onSecondaryContainer = AccentGoldContainer,
    background = PrimaryNavyDark,
    onBackground = BackgroundSlate,
    surface = Color(0xFF1E293B),
    onSurface = BackgroundSlate,
    surfaceVariant = Color(0xFF334155),
    onSurfaceVariant = SurfaceSlateBorder,
    outline = Color(0xFF475569),
    error = OverdueRed,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
