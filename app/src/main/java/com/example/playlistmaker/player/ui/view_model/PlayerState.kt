package com.example.playlistmaker.player.ui.view_model

sealed class PlayerState(
    val isFavorite: Boolean,
    val isPlayButtonEnabled: Boolean,
    val progress: String
) {

    class Default(isFavorite: Boolean) : PlayerState(isFavorite, false, PLAYBACK_DEF)

    class Prepared(isFavorite: Boolean) : PlayerState(isFavorite,true, PLAYBACK_DEF)

    class Playing(isFavorite: Boolean, progress: String) : PlayerState(isFavorite, true, progress)

    class Paused(isFavorite: Boolean, progress: String) : PlayerState(isFavorite, true, progress)

    companion object {
        const val PLAYBACK_DEF = "00:00"
    }
}
