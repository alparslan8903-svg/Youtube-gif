package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = YtRed,
    onPrimary = Color.White,
    primaryContainer = YtRedDark,
    onPrimaryContainer = Color.White,
    secondary = YtBlue,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF1E3A5F),
    onSecondaryContainer = Color(0xFFD0E4FF),
    tertiary = YtGreen,
    background = YtDarkBackground,
    onBackground = Color(0xFFF1F1F1),
    surface = YtDarkSurface,
    onSurface = Color(0xFFF1F1F1),
    surfaceVariant = YtDarkSurfaceVariant,
    onSurfaceVariant = Color(0xFFCCCCCC)
)

private val LightColorScheme = lightColorScheme(
    primary = YtRed,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFD9DF),
    onPrimaryContainer = Color(0xFF410008),
    secondary = Color(0xFF00639B),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFCDE5FF),
    onSecondaryContainer = Color(0xFF001D32),
    tertiary = YtGreen,
    background = YtLightBackground,
    onBackground = Color(0xFF1C1B1F),
    surface = YtLightSurface,
    onSurface = Color(0xFF1C1B1F),
    surfaceVariant = YtLightSurfaceVariant,
    onSurfaceVariant = Color(0xFF49454F)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
