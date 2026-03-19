package com.example.playlistmaker.presentation.search

import com.example.playlistmaker.domain.media.models.Track

data class SearchState(
    val searchText: String = "",
    val isLoading: Boolean = false,
    val empty: Boolean = false,
    val error: Boolean = false,
    val errorMessage: String = "",
    val history: List<Track> = emptyList(),
    val content: List<Track> = emptyList(),
    val isConnected: Boolean = true
)