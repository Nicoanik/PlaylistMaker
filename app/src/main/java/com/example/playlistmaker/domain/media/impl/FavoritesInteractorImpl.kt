package com.example.playlistmaker.domain.media.impl

import com.example.playlistmaker.domain.media.FavoritesInteractor
import com.example.playlistmaker.domain.media.FavoritesRepository
import com.example.playlistmaker.domain.media.models.Track
import kotlinx.coroutines.flow.Flow

class FavoritesInteractorImpl(
    private val favoritesRepository: FavoritesRepository
) : FavoritesInteractor {
    override suspend fun addToFavorites(track: Track) {
        favoritesRepository.addToFavorites(track)
    }

    override suspend fun deleteFromFavorites(trackId: Long?) {
        favoritesRepository.deleteFromFavorites(trackId)
    }

    override fun getFavorites(): Flow<List<Track>> {
        return favoritesRepository.getFavorites()
    }

    override suspend fun isTrackFavorite(trackId: Long?): Boolean {
        return trackId in favoritesRepository.getFavoritesId()
    }
}
