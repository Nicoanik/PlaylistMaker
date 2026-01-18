package com.example.playlistmaker.media.ui.view_model

import com.example.playlistmaker.media.domain.models.Playlist

sealed interface PlaylistsState {

    object Empty : PlaylistsState

    class Content(val playlists: List<Playlist>) : PlaylistsState
}
