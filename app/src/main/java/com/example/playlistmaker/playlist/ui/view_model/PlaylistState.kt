package com.example.playlistmaker.playlist.ui.view_model

import com.example.playlistmaker.media.domain.models.Playlist
import com.example.playlistmaker.media.domain.models.Track

data class PlaylistState(
    val playlist: Playlist,
    val duration: Int,
    val tracks: List<Track>
)
