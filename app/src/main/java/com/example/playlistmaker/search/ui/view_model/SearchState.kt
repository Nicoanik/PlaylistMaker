package com.example.playlistmaker.search.ui.view_model

import com.example.playlistmaker.media.domain.models.Track

sealed interface SearchState {

    object Default : SearchState
    object Loading : SearchState

    data class Content(
        val tracks: List<Track>
    ) : SearchState

    object Error : SearchState

    object Empty : SearchState

    data class History(
        val tracksHistory: List<Track>
    ) : SearchState
}
