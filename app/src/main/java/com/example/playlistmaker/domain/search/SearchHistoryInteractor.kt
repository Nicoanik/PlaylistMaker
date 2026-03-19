package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.media.models.Track

interface SearchHistoryInteractor {
    fun addTrackToSearchHistory(track: Track)
    fun getSearchHistory(): List<Track>
    fun clearSearchHistory()
}
