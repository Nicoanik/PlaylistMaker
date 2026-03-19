package com.example.playlistmaker.ui.screens.media

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.domain.media.models.Track
import com.example.playlistmaker.ui.components.TrackItem
import com.example.playlistmaker.ui.screens.search.dismissKeyboardOnScroll
import java.util.UUID

@Composable
fun FavoriteScreen(
    tracks: List<Track>,
    onTrackClick: (Track) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
                .dismissKeyboardOnScroll()
        ) {
            items(tracks, key = { it.trackId ?: UUID.randomUUID() }) { track ->
                TrackItem(track) { onTrackClick(track) }
            }
        }
    }
}