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

    private val tracks: MutableList<Track> = mutableListOf()

    init {
        getSearchHistory()
    }

    override fun addTrackToSearchHistory(track: Track) {
        getSearchHistory()
        tracks.remove(track)
        tracks.add(0, track)
        if (tracks.size > 10) tracks.removeAt(10)
        saveSearchHistory()
    }

    override fun saveSearchHistory() {
        val json = Gson().toJson(tracks)
        sharedPrefs.edit { putString(SEARCH_HISTORY_KEY, json) }
    }

    override fun getSearchHistory(): List<Track> {
        tracks.clear()
        if (sharedPrefs.contains(SEARCH_HISTORY_KEY)) {
            val json = sharedPrefs.getString(SEARCH_HISTORY_KEY, "")
            tracks.addAll(Gson().fromJson(json, Array<Track>::class.java))
        }
        return tracks
    }

    override fun clearSearchHistory() {
        sharedPrefs.edit { remove(SEARCH_HISTORY_KEY) }
        tracks.clear()
    }
}
