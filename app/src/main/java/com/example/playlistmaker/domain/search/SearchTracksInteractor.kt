package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.media.models.Track
import kotlinx.coroutines.flow.Flow

interface SearchTracksInteractor {
    fun searchTracks(expression: String): Flow<Pair<List<Track>?, String?>>
}
