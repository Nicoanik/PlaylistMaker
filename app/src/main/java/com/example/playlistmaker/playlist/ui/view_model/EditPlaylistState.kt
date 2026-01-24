package com.example.playlistmaker.playlist.ui.view_model

import com.example.playlistmaker.media.domain.models.Playlist

sealed interface EditPlaylistState {

    class Content(val playlist: Playlist) : EditPlaylistState

    class Done() : EditPlaylistState
}
