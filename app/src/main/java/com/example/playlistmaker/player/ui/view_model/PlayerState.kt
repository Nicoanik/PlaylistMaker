package com.example.playlistmaker.player.ui.view_model

sealed class PlayerState(val isPlayButtonEnabled: Boolean, val progress: String) {

    class Default : PlayerState(false, PLAYBACK_DEF)

    class Prepared : PlayerState(true, PLAYBACK_DEF)

    class Playing(progress: String) : PlayerState(true, progress)

    class Paused(progress: String) : PlayerState(true, progress)

    companion object {
        const val PLAYBACK_DEF = "00:00"
    }
}
