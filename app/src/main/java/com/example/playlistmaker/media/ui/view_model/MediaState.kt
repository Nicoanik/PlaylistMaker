package com.example.playlistmaker.media.ui.view_model

sealed interface MediaState {

    object Empty: MediaState

    object NotEmpty: MediaState
}