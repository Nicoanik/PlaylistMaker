package com.example.playlistmaker.player.ui.view_model

import com.example.playlistmaker.media.domain.models.Playlist

sealed interface PlayerState {

    class Prepared() : PlayerState

    class Playing(val progress: String) : PlayerState

    class Paused(val progress: String) : PlayerState

    class IsFavorite(val isFavorite: Boolean) : PlayerState

    class BottomSheetContent(val playlists: List<Playlist>) : PlayerState

    class InPlaylist(val inPlaylist: Boolean, val title: String) : PlayerState
}
