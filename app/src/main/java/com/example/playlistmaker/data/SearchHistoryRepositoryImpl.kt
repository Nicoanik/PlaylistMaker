package com.example.playlistmaker.data

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.playlistmaker.domain.api.SearchHistoryRepository
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson

class SearchHistoryRepositoryImpl(private val sharedPrefs: SharedPreferences) : SearchHistoryRepository {

    companion object {
        const val SEARCH_HISTORY_KEY = "search_history_key"
    }

    private val tracksHistory: MutableList<Track> = mutableListOf()

    init {
        getSearchHistory()
    }

    override fun addTrackToSearchHistory(track: Track) {
        getSearchHistory()
        tracksHistory.remove(track)
        tracksHistory.add(0, track)
        if (tracksHistory.size > 10) tracksHistory.removeAt(10)
        saveSearchHistory()
    }

    override fun saveSearchHistory() {
        val json = Gson().toJson(tracksHistory)
        sharedPrefs.edit { putString(SEARCH_HISTORY_KEY, json) }
    }

    override fun getSearchHistory(): List<Track> {
        if (sharedPrefs.contains(SEARCH_HISTORY_KEY)) {
            tracksHistory.clear()
            val json = sharedPrefs.getString(SEARCH_HISTORY_KEY, "")
            tracksHistory.addAll(Gson().fromJson(json, Array<Track>::class.java))
        }
        return tracksHistory
    }

    override fun clearSearchHistory() {
        sharedPrefs.edit { remove(SEARCH_HISTORY_KEY) }
        tracksHistory.clear()
    }
}
