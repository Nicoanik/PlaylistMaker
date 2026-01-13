package com.example.playlistmaker.media.domain

import android.net.Uri
import com.example.playlistmaker.media.domain.models.Playlist
import com.example.playlistmaker.media.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {

    suspend fun addPlaylist(playlist: Playlist)

    fun getPlaylists(): Flow<List<Playlist>>

    fun saveImageToPrivateStorage(uri: Uri)

    suspend fun addTrackToPlaylist(track: Track, playlist: Playlist)
}
