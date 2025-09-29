package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.domain.models.Resource
import com.example.playlistmaker.search.domain.models.Track

interface SearchTracksRepository {
    fun searchTracks(expression: String): Resource<List<Track>>
}