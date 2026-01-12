package com.example.playlistmaker.player.ui.view_model

import com.example.playlistmaker.media.domain.models.Playlist

sealed class PlayerState(
    val isFavorite: Boolean,
    val isPlayButtonEnabled: Boolean,
    val progress: String,
    val playlists: List<Playlist>
) {

    class Prepared(isFavorite: Boolean) :
        PlayerState(isFavorite, true, PLAYBACK_DEF, emptyList())

    class Playing(isFavorite: Boolean, progress: String) :
        PlayerState(isFavorite, true, progress, emptyList())

    class Paused(isFavorite: Boolean, progress: String) :
        PlayerState(isFavorite, true, progress, emptyList())

    class BottomSheetContent(isFavorite: Boolean, progress: String, playlists: List<Playlist>) :
        PlayerState(isFavorite, true, progress, playlists)

    companion object {
        const val PLAYBACK_DEF = "00:00"
    }
}
