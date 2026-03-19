package com.example.playlistmaker.domain.media

import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesInteractor {

    suspend fun addToFavorites(track: Track)

    suspend fun deleteFromFavorites(trackId: Long?)

    fun getFavorites(): Flow<List<Track>>

    suspend fun isTrackFavorite(trackId: Long?): Boolean
}
