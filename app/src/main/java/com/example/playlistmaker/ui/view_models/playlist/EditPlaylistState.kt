package com.example.playlistmaker.ui.view_models.playlist

import com.example.playlistmaker.domain.media.models.Playlist

sealed interface EditPlaylistState {

    class Content(val playlist: Playlist) : EditPlaylistState

    class Done() : EditPlaylistState
}
