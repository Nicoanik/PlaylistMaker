package com.example.playlistmaker.search.ui.view_model

import com.example.playlistmaker.search.domain.models.Track

sealed interface SearchState {

    object Loading : SearchState

    data class Content(
        val tracks: List<Track>
    ) : SearchState

    object Error: SearchState

    object Empty : SearchState

    data class History(
        val tracksHistory: List<Track>
    ) : SearchState
}
