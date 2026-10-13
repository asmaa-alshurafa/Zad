package com.example.zad.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = NavyPrimary,
    onPrimary = SurfaceWhite,
    primaryContainer = NavyAccent,
    onPrimaryContainer = NavyDark,
    secondary = TextSecondary,
    onSecondary = SurfaceWhite,
    secondaryContainer = NavyAccent,
    onSecondaryContainer = NavyDark,
    tertiary = GreenFree,
    onTertiary = SurfaceWhite,
    tertiaryContainer = GreenFreeContainer,
    onTertiaryContainer = GreenFree,
    background = BackgroundLight,
    onBackground = TextPrimary,
    surface = SurfaceWhite,
    onSurface = TextPrimary,
    surfaceVariant = NavyAccent,
    onSurfaceVariant = TextSecondary,
    outline = BorderColor,
    outlineVariant = BorderColor
)

/**
 * ZadTheme applies the application's clean, modern navy-blue light color scheme.
 * Dynamic color is disabled by default to ensure brand consistency across all Android 12+ devices.
 */
@Composable
fun ZadTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}