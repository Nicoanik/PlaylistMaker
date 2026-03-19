package com.example.playlistmaker.presentation.media

import androidx.compose.runtime.Immutable
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track

@Immutable
data class MediaState(
    val favoriteTracks: List<Track> = emptyList(),
    val playlists: List<Playlist> = emptyList()
)