package com.example.playlistmaker.ui.view_models.media

import com.example.playlistmaker.domain.media.models.Playlist

sealed interface PlaylistsState {

    object Empty : PlaylistsState

    class Content(val playlists: List<Playlist>) : PlaylistsState
}
