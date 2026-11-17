package com.morsetranslatorpro.app.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColors = lightColorScheme(
    primary = BluePrimary,
    secondary = BlueSecondary,
    tertiary = AmberTertiary
)

private val DarkColors = darkColorScheme(
    primary = BluePrimaryDark,
    secondary = BlueSecondaryDark,
    tertiary = AmberTertiaryDark
)

/**
 * App theme. Supports Material You dynamic color on Android 12+, with a manual
 * light/dark override and a large-text scaling option for accessibility.
 */
@Composable
fun MorseTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    largeText: Boolean = false,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ->
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        darkTheme -> DarkColors
        else -> LightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = if (largeText) LargeTypography else AppTypography,
        content = content
    )
}
