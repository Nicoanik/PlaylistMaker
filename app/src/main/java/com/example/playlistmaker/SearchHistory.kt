package com.example.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson

class SearchHistory(val sharedPrefs: SharedPreferences) {

    val tracks: MutableList<Track> = mutableListOf()

    init {
        getSearchHistory()
    }

    fun addTrackToSearchHistory(track: Track) {
        getSearchHistory()
        tracks.remove(track)
        tracks.add(0, track)
        if (tracks.size > 10) tracks.removeAt(10)
        saveSearchHistory()
    }

    private fun saveSearchHistory() {
        val json = Gson().toJson(tracks)
        sharedPrefs.edit().putString(SEARCH_HISTORY_KEY, json).apply()
    }

    fun getSearchHistory() {
        tracks.clear()
        if (sharedPrefs.contains(SEARCH_HISTORY_KEY)) {
            val json = sharedPrefs.getString(SEARCH_HISTORY_KEY, "")
            tracks.addAll(Gson().fromJson(json, Array<Track>::class.java))
        }
    }

    fun clearSearchHistory() {
        sharedPrefs.edit().remove(SEARCH_HISTORY_KEY).apply()
        tracks.clear()
    }

    companion object {
        const val SEARCH_HISTORY_KEY = "search_history_key"
    }
}