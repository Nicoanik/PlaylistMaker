package com.example.playlistmaker.media.domain.db

import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesInteractor {

    suspend fun addToFavorites(track: Track)

    suspend fun deleteFromFavorites(trackId: Int?)

    fun getFavorites(): Flow<List<Track>>

    suspend fun isTrackFavorite(trackId: Int?): Boolean
}
