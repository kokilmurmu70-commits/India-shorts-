package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = SaffronPrimary,
    onPrimary = Color.White,
    primaryContainer = SaffronDark,
    onPrimaryContainer = Color.White,
    secondary = EmeraldSecondary,
    onSecondary = Color.White,
    secondaryContainer = EmeraldDark,
    onSecondaryContainer = Color.White,
    tertiary = AccentGold,
    onTertiary = NavyDeep,
    background = NavyDeep,
    onBackground = TextWhite,
    surface = NavyCard,
    onSurface = TextWhite,
    surfaceVariant = NavySurface,
    onSurfaceVariant = TextSecondary,
    outline = NavyBorder,
    error = AccentRed,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = SaffronPrimary,
    onPrimary = Color.White,
    primaryContainer = SaffronLight,
    onPrimaryContainer = NavyDeep,
    secondary = EmeraldSecondary,
    onSecondary = Color.White,
    secondaryContainer = EmeraldLight,
    onSecondaryContainer = NavyDeep,
    tertiary = AccentGold,
    onTertiary = NavyDeep,
    background = LightBg,
    onBackground = LightTextPrimary,
    surface = LightSurface,
    onSurface = LightTextPrimary,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightTextSecondary,
    outline = Color(0xFFCBD5E1),
    error = AccentRed,
    onError = Color.White
)

@Composable
fun IndiaShortsTheme(
    darkTheme: Boolean = true, // Default to dark theme for modern video streaming
    dynamicColor: Boolean = false, // Keep brand identity prominent
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
