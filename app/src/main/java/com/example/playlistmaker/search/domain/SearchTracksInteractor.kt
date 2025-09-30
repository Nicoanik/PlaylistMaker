package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.domain.models.Resource
import com.example.playlistmaker.search.domain.models.Track

interface SearchTracksInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(foundTracks: List<Track>?, errorMessage: String?)
    }
}