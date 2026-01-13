package com.example.playlistmaker.media.domain.impl

import android.net.Uri
import com.example.playlistmaker.media.domain.PlaylistInteractor
import com.example.playlistmaker.media.domain.PlaylistRepository
import com.example.playlistmaker.media.domain.models.Playlist
import com.example.playlistmaker.media.domain.models.Track
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(
    private val playlistRepository: PlaylistRepository
) : PlaylistInteractor {

    override suspend fun addPlaylist(playlist: Playlist) {
        playlistRepository.addPlaylist(playlist)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return playlistRepository.getPlaylists()
    }

    override fun saveImageToPrivateStorage(uri: Uri) {
        playlistRepository.saveImageToPrivateStorage(uri)
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        playlistRepository.addTrackToPlaylist(track, playlist)
    }
}
