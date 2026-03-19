package com.example.playlistmaker.ui.view_models.media

import com.example.playlistmaker.domain.media.models.Track

sealed interface FavoriteState {

    object Empty : FavoriteState

    class Content(val tracks: List<Track>) : FavoriteState
}
