package com.example.playlistmaker.search.data.impl

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.data.SearchHistoryRepository
import com.google.gson.Gson

class SearchHistoryRepositoryImpl(private val sharedPrefs: SharedPreferences) :
    SearchHistoryRepository {

    private val tracksHistory: MutableList<Track> = mutableListOf()
    private val gson = Gson()

    init {
        getSearchHistory()
    }

    override fun addTrackToSearchHistory(track: Track) {
        getSearchHistory()
        tracksHistory.remove(track)
        tracksHistory.add(0, track)
        if (tracksHistory.size > MAX_SEARCH_HISTORY) tracksHistory.removeAt(10)
        saveSearchHistory()
    }

    override fun saveSearchHistory() {
        sharedPrefs.edit { putString(SEARCH_HISTORY_KEY, gson.toJson(tracksHistory)) }
    }

    override fun getSearchHistory(): List<Track> {
        if (sharedPrefs.contains(SEARCH_HISTORY_KEY)) {
            tracksHistory.clear()
            val json = sharedPrefs.getString(SEARCH_HISTORY_KEY, "")
            tracksHistory.addAll(gson.fromJson(json, Array<Track>::class.java))
        }
        return tracksHistory
    }

    override fun clearSearchHistory() {
        sharedPrefs.edit { remove(SEARCH_HISTORY_KEY) }
        tracksHistory.clear()
    }

    companion object {
        const val SEARCH_HISTORY_KEY = "search_history_key"
        const val MAX_SEARCH_HISTORY = 10
    }
}