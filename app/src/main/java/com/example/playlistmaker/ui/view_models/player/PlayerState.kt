package com.example.playlistmaker.ui.view_models.player

import com.example.playlistmaker.domain.media.models.Playlist

sealed interface PlayerState {

    class Default : PlayerState

    class Prepared : PlayerState

    class Playing(val progress: String) : PlayerState

    class Paused(val progress: String) : PlayerState

    class IsFavorite(val isFavorite: Boolean) : PlayerState

    class BottomSheetContent(val playlists: List<Playlist>) : PlayerState

    class InPlaylist(val inPlaylist: Boolean, val title: String) : PlayerState

}