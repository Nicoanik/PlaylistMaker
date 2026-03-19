package com.example.playlistmaker.presentation.media

import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track

data class MediaState(
    val favoriteTracks: List<Track> = emptyList(),
    val playlists: List<Playlist> = emptyList()
)