package com.example.playlistmaker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

@Composable
fun PlaylistMakerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}

private val LightColorScheme = lightColorScheme(
    background = ypWhite,
    onBackground = ypBlack,
    surface = ypWhite,
    onSurface = ypBlack,
    surfaceVariant = ypLightGray,
    onSurfaceVariant = ypTextGray,
    onSecondary = ypTextGray,
    tertiary = ypLightGray,
    onTertiary = ypTextGray
)

private val DarkColorScheme = darkColorScheme(
    background = ypBlack,
    onBackground = ypWhite,
    surface = ypBlack,
    onSurface = ypWhite,
    surfaceVariant = ypWhite,
    onSurfaceVariant = ypBlack,
    onSecondary = ypWhite,
    tertiary = switchTrackDark,
    onTertiary = ypBlue
)