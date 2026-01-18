package com.example.playlistmaker.playlist.ui.view_model

import com.example.playlistmaker.media.domain.models.Playlist

sealed interface PlaylistState {

    class Content(val playlist: Playlist, val duration: Int) : PlaylistState
}
