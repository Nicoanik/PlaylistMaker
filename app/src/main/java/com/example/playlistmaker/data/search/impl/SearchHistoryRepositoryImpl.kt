package com.example.playlistmaker.data.search.impl

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.search.SearchHistoryRepository
import com.google.gson.Gson

class SearchHistoryRepositoryImpl(
    private val sharedPrefs: SharedPreferences,
    private val gson: Gson
) : SearchHistoryRepository {

    private val tracksHistory: MutableList<Track> = mutableListOf()

    override fun addTrackToSearchHistory(track: Track) {
        tracksHistory.remove(track)
        tracksHistory.add(0, track)
        if (tracksHistory.size > MAX_SEARCH_HISTORY) tracksHistory.removeAt(MAX_SEARCH_HISTORY)
        sharedPrefs.edit { putString(SEARCH_HISTORY_KEY, gson.toJson(tracksHistory)) }
    }

    override fun getSearchHistory(): List<Track> {
        if (sharedPrefs.contains(SEARCH_HISTORY_KEY)) {
            tracksHistory.clear()
            val json = sharedPrefs.getString(SEARCH_HISTORY_KEY, "")
            tracksHistory.addAll(gson.fromJson(json, Array<Track>::class.java).toList())
        }
        return tracksHistory.toList()
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
