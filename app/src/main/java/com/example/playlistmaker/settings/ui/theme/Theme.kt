package com.example.playlistmaker.settings.ui.theme

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
    background = YP_white,
    onBackground = YP_black,
    surface = YP_white,
    onSurface = YP_black,
    surfaceVariant = YP_light_gray,
    onSurfaceVariant = YP_text_gray
)

private val DarkColorScheme = darkColorScheme(
    background = YP_black,
    onBackground = YP_white,
    surface = YP_black,
    onSurface = YP_white,
    surfaceVariant = YP_white,
    onSurfaceVariant = YP_black
)