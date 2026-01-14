package com.example.playlistmaker.media.ui.view_model

import com.example.playlistmaker.media.domain.models.Playlist

sealed interface PlaylistState {

    object Empty : PlaylistState

    class Content(val playlists: List<Playlist>) : PlaylistState
}
