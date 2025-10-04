package com.example.playlistmaker.search.ui.view_model

import com.example.playlistmaker.search.domain.models.Track

sealed interface SearchState {

    object Loading : SearchState

    data class Content(
        val tracks: List<Track>
    ) : SearchState

    data class Error(
        val errorMessage: String
    ) : SearchState

    data class Empty(
        val message: String
    ) : SearchState

    data class History(
        val tracksHistory: List<Track>
    ) : SearchState
}
