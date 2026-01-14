package com.example.playlistmaker.search.domain

import com.example.playlistmaker.media.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface SearchTracksInteractor {
    fun searchTracks(expression: String): Flow<Pair<List<Track>?, String?>>
}
