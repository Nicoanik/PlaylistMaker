package com.example.playlistmaker.player.ui.view_model

import com.example.playlistmaker.media.domain.models.Playlist
sealed interface PlayerState {

    class Prepared(val isPlayButtonEnabled: Boolean) : PlayerState

    class Playing(val progress: String) : PlayerState

    class Paused(val progress: String) : PlayerState

    class Favorite(val isFavorite: Boolean) : PlayerState

    class BottomSheetContent(val playlists: List<Playlist>) : PlayerState

    class InPlaylist(val inPlaylist: Boolean) : PlayerState

    companion object {
        const val PLAYBACK_DEF = "00:00"
    }
}
