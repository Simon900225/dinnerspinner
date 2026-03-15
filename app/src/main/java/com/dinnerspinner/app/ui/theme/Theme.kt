package com.dinnerspinner.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CasinoColorScheme = darkColorScheme(
    primary = CasinoGold,
    onPrimary = CasinoBlack,
    primaryContainer = CasinoDarkSurface,
    onPrimaryContainer = CasinoGold,
    secondary = CasinoGoldDim,
    onSecondary = CasinoBlack,
    secondaryContainer = CasinoCardBg,
    onSecondaryContainer = CasinoGoldDim,
    background = CasinoBlack,
    onBackground = CasinoWhite,
    surface = CasinoDarkSurface,
    onSurface = CasinoWhite,
    surfaceVariant = CasinoCardBg,
    onSurfaceVariant = CasinoGoldDark,
    outline = CasinoGoldVeryDark,
    outlineVariant = CasinoBorder,
    tertiary = CasinoGreen,
    onTertiary = CasinoBlack,
    error = Color(0xFFE05050),
    onError = CasinoBlack,
)

@Composable
fun DinnerSpinnerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = CasinoColorScheme,
        typography = Typography,
        content = content
    )
}
