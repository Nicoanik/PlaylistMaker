package com.example.playlistmaker.media.ui.view_model

sealed interface PlaylistState {

    object Empty: PlaylistState

    object Content: PlaylistState
}
