package com.example.playlistmaker.media.data.impl

import com.example.playlistmaker.media.data.converters.TrackDbConverter
import com.example.playlistmaker.media.data.db.AppDatabase
import com.example.playlistmaker.media.data.db.entity.TrackEntity
import com.example.playlistmaker.media.domain.db.FavoritesRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritesRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConverter: TrackDbConverter
) : FavoritesRepository {
    override suspend fun addToFavorites(track: Track) {
        appDatabase.trackDao().insertTrack(trackDbConverter.map(track))
    }

    override suspend fun deleteFromFavorites(trackId: Int?) {
        appDatabase.trackDao().deleteTrack(trackId)
    }

    override fun getFavorites(): Flow<List<Track>> =
        appDatabase.trackDao().getTracks().map { tracks ->
            convertFromTrackEntity(tracks)
        }

    override suspend fun getFavoritesId(): List<Int> {
        return appDatabase.trackDao().getTracksId()
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> trackDbConverter.map(track) }
    }
}
