package com.example.playlistmaker.ui.screens.media

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun PlaylistsScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize() // для визуализации
    ) {
        Text(
            text = "Второй экран!",
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}