package com.example.playlistmaker.search.ui.view_model

import com.example.playlistmaker.media.domain.models.Track

data class SearchState(
    val searchText: String = "",
    val isLoading: Boolean = false,
    val empty: Boolean = false,
    val error: Boolean = false,
    val history: List<Track> = emptyList(),
    val content: List<Track> = emptyList(),
    val toastMessage: String = "",
    val isConnected: Boolean = false
)