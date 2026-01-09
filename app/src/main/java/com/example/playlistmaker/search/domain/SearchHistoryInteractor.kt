package com.example.playlistmaker.search.domain

import com.example.playlistmaker.media.domain.models.Track

interface SearchHistoryInteractor {
    fun addTrackToSearchHistory(track: Track)
    fun getSearchHistory(): List<Track>
    fun clearSearchHistory()
}
