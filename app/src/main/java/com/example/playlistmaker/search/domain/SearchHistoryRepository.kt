package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.domain.models.Track

interface SearchHistoryRepository {
    fun addTrackToSearchHistory(track: Track)
    fun saveSearchHistory()
    fun getSearchHistory(): List<Track>
    fun clearSearchHistory()
}
