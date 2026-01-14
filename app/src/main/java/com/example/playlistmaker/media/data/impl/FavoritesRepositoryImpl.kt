package com.example.playlistmaker.media.data.impl

import com.example.playlistmaker.media.data.converters.TrackDbConverter
import com.example.playlistmaker.media.data.db.dao.FavoriteTrackDao
import com.example.playlistmaker.media.data.db.entity.FavoriteTrackEntity
import com.example.playlistmaker.media.domain.FavoritesRepository
import com.example.playlistmaker.media.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class FavoritesRepositoryImpl(
    private val favoriteTrackDao: FavoriteTrackDao,
    private val trackDbConverter: TrackDbConverter
) : FavoritesRepository {
    override suspend fun addToFavorites(track: Track) {
        favoriteTrackDao.insertTrack(trackDbConverter.map(track))
    }

    override suspend fun deleteFromFavorites(trackId: Int?) {
        favoriteTrackDao.deleteTrack(trackId)
    }

    override fun getFavorites(): Flow<List<Track>> =
        favoriteTrackDao.getTracks()
            .map { tracks -> convertFromTrackEntity(tracks) }
            .distinctUntilChanged()

    override suspend fun getFavoritesId(): List<Int> {
        return favoriteTrackDao.getTracksId()
    }

    private fun convertFromTrackEntity(tracks: List<FavoriteTrackEntity>): List<Track> {
        return tracks.map { track -> trackDbConverter.map(track) }
    }
}
