package com.example.playlistmaker.domain.media

import android.net.Uri
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {

    suspend fun insertPlaylist(playlist: Playlist)

    fun getPlaylists(): Flow<List<Playlist>>

    fun getPlaylistById(playlistId: Long): Flow<Playlist>

    fun saveImageToPrivateStorage(uri: Uri): String

    suspend fun addTrackToPlaylist(track: Track, playlist: Playlist)

    fun getTracksByIds(trackIds: List<Long>): Flow<List<Track>>

    suspend fun deleteTrackFromPlaylist(trackId: Long, playlist: Playlist)

    suspend fun sharePlaylist(playlist: Playlist, tracks: List<Track>)

    suspend fun deletePlaylistById(playlistId: Long)
}
