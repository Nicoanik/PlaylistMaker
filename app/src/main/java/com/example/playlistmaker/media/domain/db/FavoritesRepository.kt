package com.example.playlistmaker.media.domain.db

import com.example.playlistmaker.media.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {

    suspend fun addToFavorites(track: Track)

    suspend fun deleteFromFavorites(trackId: Int?)

    fun getFavorites(): Flow<List<Track>>

    suspend fun getFavoritesId(): List<Int>
}
