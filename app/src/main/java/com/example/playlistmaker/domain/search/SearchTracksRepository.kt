package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.models.Resource
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface SearchTracksRepository {
    fun searchTracks(expression: String): Flow<Resource<List<Track>>>
}
