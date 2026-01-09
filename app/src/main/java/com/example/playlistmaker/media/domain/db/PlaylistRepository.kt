package com.example.playlistmaker.media.domain.db

import com.example.playlistmaker.media.domain.models.Playlist
import com.example.playlistmaker.media.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    suspend fun addPlaylist(playlist: Playlist)

    fun getPlaylists(): Flow<List<Playlist>>

    suspend fun addTrackToPlaylist(track: Track, playlist: Playlist)
}
