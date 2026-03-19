package com.example.playlistmaker.domain.media

import com.example.playlistmaker.domain.media.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {

    suspend fun addToFavorites(track: Track)

    suspend fun deleteFromFavorites(trackId: Long?)

    fun getFavorites(): Flow<List<Track>>

    suspend fun getFavoritesId(): List<Long>
}
