package com.example.playlistmaker.search.data.impl

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import com.example.playlistmaker.media.domain.models.Track
import com.example.playlistmaker.search.domain.SearchHistoryRepository
import com.google.gson.Gson

class SearchHistoryRepositoryImpl(
    private val sharedPrefs: SharedPreferences,
    private val gson: Gson
) : SearchHistoryRepository {

    private val tracksHistory: MutableList<Track> = mutableListOf()

    override fun addTrackToSearchHistory(track: Track) {
        Log.d("Nico", "Before add: ${tracksHistory.size} tracks, hash: ${System.identityHashCode(tracksHistory)}")
        tracksHistory.remove(track)
        tracksHistory.add(0, track)
        if (tracksHistory.size > MAX_SEARCH_HISTORY) tracksHistory.removeAt(MAX_SEARCH_HISTORY)
        sharedPrefs.edit { putString(SEARCH_HISTORY_KEY, gson.toJson(tracksHistory)) }
        Log.d("Nico", "After add: ${tracksHistory.size} tracks, hash: ${System.identityHashCode(tracksHistory)}")
    }

    override fun getSearchHistory(): List<Track> {
        if (sharedPrefs.contains(SEARCH_HISTORY_KEY)) {
            val json = sharedPrefs.getString(SEARCH_HISTORY_KEY, "")
            return gson.fromJson(json, Array<Track>::class.java).toList()
        }
        Log.d("Nico", "getSearchHistory returned: ${tracksHistory.size} tracks, hash: ${System.identityHashCode(tracksHistory)}")
        return emptyList()
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
