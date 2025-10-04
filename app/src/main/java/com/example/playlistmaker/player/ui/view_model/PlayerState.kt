package com.example.playlistmaker.player.ui.view_model

sealed interface PlayerState {

    data class Prepared(
        val timer: String
    ) : PlayerState

    data class Playing(
        val timer: String
        ) : PlayerState

    data class Paused(
        val timer: String
    ) : PlayerState
}
