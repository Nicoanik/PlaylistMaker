package com.example.playlistmaker.player.ui.view_model

sealed interface PlayerState {

    object Default : PlayerState

    object Prepared : PlayerState

    data class Playing(
        val timer: String
        ) : PlayerState

    object Paused : PlayerState
}
