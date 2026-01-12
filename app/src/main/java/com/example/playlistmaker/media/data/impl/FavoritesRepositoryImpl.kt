package com.example.playlistmaker.media.data.impl

import com.example.playlistmaker.media.data.converters.TrackDbConverter
import com.example.playlistmaker.media.data.db.AppDatabase
import com.example.playlistmaker.media.data.db.entity.FavoriteTrackEntity
import com.example.playlistmaker.media.domain.FavoritesRepository
import com.example.playlistmaker.media.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritesRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConverter: TrackDbConverter
) : FavoritesRepository {
    override suspend fun addToFavorites(track: Track) {
        appDatabase.favoriteTrackDao().insertTrack(trackDbConverter.map(track))
    }

    override suspend fun deleteFromFavorites(trackId: Int?) {
        appDatabase.favoriteTrackDao().deleteTrack(trackId)
    }

    override fun getFavorites(): Flow<List<Track>> =
        appDatabase.favoriteTrackDao().getTracks().map { tracks ->
            convertFromTrackEntity(tracks)
        }

    override suspend fun getFavoritesId(): List<Int> {
        return appDatabase.favoriteTrackDao().getTracksId()
    }

    private fun convertFromTrackEntity(tracks: List<FavoriteTrackEntity>): List<Track> {
        return tracks.map { track -> trackDbConverter.map(track) }
    }
}
