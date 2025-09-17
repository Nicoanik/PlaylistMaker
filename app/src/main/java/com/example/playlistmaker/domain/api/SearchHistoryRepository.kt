package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface SearchHistoryRepository {

    fun addTrackToSearchHistory(track: Track)

    fun saveSearchHistory()

    fun getSearchHistory(): List<Track>

    fun clearSearchHistory()
}