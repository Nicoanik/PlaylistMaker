package com.example.playlistmaker.media.ui.view_model

import com.example.playlistmaker.search.domain.models.Track

sealed interface FavoriteState {

    object Empty: FavoriteState

    class Content(val tracks: List<Track>): FavoriteState
}
