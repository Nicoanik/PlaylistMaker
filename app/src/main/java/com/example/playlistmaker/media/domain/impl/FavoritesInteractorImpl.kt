package com.example.playlistmaker.media.domain.impl

import com.example.playlistmaker.media.domain.FavoritesInteractor
import com.example.playlistmaker.media.domain.FavoritesRepository
import com.example.playlistmaker.media.domain.models.Track
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
