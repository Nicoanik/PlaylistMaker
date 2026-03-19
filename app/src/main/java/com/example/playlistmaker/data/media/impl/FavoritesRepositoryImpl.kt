package com.example.playlistmaker.data.media.impl

import com.example.playlistmaker.data.media.converters.TrackDbConverter
import com.example.playlistmaker.data.media.db.dao.FavoriteTrackDao
import com.example.playlistmaker.data.media.db.entity.FavoriteTrackEntity
import com.example.playlistmaker.domain.media.FavoritesRepository
import com.example.playlistmaker.domain.models.Track
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

    override suspend fun deleteFromFavorites(trackId: Long?) {
        favoriteTrackDao.deleteTrack(trackId)
    }

    override fun getFavorites(): Flow<List<Track>> =
        favoriteTrackDao.getTracks()
            .map { tracks -> convertFromTrackEntity(tracks) }
            .distinctUntilChanged()

    override suspend fun getFavoritesId(): List<Long> {
        return favoriteTrackDao.getTracksId()
    }

    private fun convertFromTrackEntity(tracks: List<FavoriteTrackEntity>): List<Track> {
        return tracks.map { track -> trackDbConverter.map(track) }
    }
}
