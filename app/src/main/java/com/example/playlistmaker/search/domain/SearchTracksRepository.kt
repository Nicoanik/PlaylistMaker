package com.example.playlistmaker.search.domain

import com.example.playlistmaker.media.domain.models.Resource
import com.example.playlistmaker.media.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface SearchTracksRepository {
    fun searchTracks(expression: String): Flow<Resource<List<Track>>>
}
