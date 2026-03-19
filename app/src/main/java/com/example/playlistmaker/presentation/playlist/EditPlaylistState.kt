package com.example.playlistmaker.presentation.playlist

import com.example.playlistmaker.domain.models.Playlist

sealed interface EditPlaylistState {

    class Content(val playlist: Playlist) : EditPlaylistState

    class Done() : EditPlaylistState
}
