package com.example.playlistmaker.presentation.playlist

import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track

sealed interface PlaylistState {

    class Content(
        val playlist: Playlist,
        val duration: Int,
        val tracks: List<Track>
    ) : PlaylistState

    class Share(val isSharing: Boolean) : PlaylistState
}
