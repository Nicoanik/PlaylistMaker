package com.example.playlistmaker.playlist.ui.view_model

import com.example.playlistmaker.media.domain.models.Playlist
import com.example.playlistmaker.media.domain.models.Track

sealed interface PlaylistState {

    class Content(
        val playlist: Playlist,
        val duration: Int,
        val tracks: List<Track>
    ) : PlaylistState

    class Share(val isSharing: Boolean) : PlaylistState
}
