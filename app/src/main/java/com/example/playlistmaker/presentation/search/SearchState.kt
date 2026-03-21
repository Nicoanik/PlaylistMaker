package com.example.playlistmaker.presentation.search

import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class SearchState(
    val searchText: String = "",
    val isLoading: Boolean = false,
    val empty: Boolean = false,
    val error: Boolean = false,
    val errorMessage: String = "",
    val history: ImmutableList<Track> = persistentListOf(),
    val content: ImmutableList<Track> = persistentListOf(),
    val isConnected: Boolean = true
)