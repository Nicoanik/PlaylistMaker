package com.example.playlistmaker.media.data.impl

import com.example.playlistmaker.media.data.converters.PlaylistDbConverter
import com.example.playlistmaker.media.data.converters.PlaylistTrackDbConvertor
import com.example.playlistmaker.media.data.db.AppDatabase
import com.example.playlistmaker.media.data.db.entity.PlaylistEntity
import com.example.playlistmaker.media.domain.PlaylistRepository
import com.example.playlistmaker.media.domain.models.Playlist
import com.example.playlistmaker.media.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.collections.map

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistBdConverter: PlaylistDbConverter,
    private val playlistTrackDbConverter: PlaylistTrackDbConvertor
) : PlaylistRepository {

    override suspend fun addPlaylist(playlist: Playlist) {
        appDatabase.playlistDao().addPlaylist(playlistBdConverter.map(playlist))
    }

    override fun getPlaylists(): Flow<List<Playlist>> =
        appDatabase.playlistDao().getPlaylists().map { playlists ->
            convertFromPlaylistEntity(playlists)
        }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        withContext(Dispatchers.IO) {
            appDatabase.playlistTrackDao().insertTrack(playlistTrackDbConverter.map(track))
            appDatabase.playlistDao().addPlaylist(
                playlistBdConverter.map(
                    playlist.copy(
                        trackIds = playlist.trackIds + track.trackId!!.toLong(),
                        playlistSize = playlist.playlistSize + 1
                    )
                )
            )
        }
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlist -> playlistBdConverter.map(playlist) }
    }
}