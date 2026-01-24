package com.example.playlistmaker.media.domain.impl

import android.content.Context
import android.net.Uri
import com.example.playlistmaker.R
import com.example.playlistmaker.media.domain.PlaylistInteractor
import com.example.playlistmaker.media.domain.PlaylistRepository
import com.example.playlistmaker.media.domain.models.Playlist
import com.example.playlistmaker.media.domain.models.Track
import com.example.playlistmaker.media.domain.models.timeConversion
import com.example.playlistmaker.sharing.ExternalNavigator
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(
    private val playlistRepository: PlaylistRepository,
    private val externalNavigator: ExternalNavigator,
    private val context: Context
) : PlaylistInteractor {

    override suspend fun insertPlaylist(playlist: Playlist) {
        playlistRepository.insertPlaylist(playlist)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return playlistRepository.getPlaylists()
    }

    override fun getPlaylistById(playlistId: Long): Flow<Playlist> {
        return playlistRepository.getPlaylistById(playlistId)
    }

    override fun saveImageToPrivateStorage(uri: Uri): String {
        return playlistRepository.saveImageToPrivateStorage(uri)
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        playlistRepository.addTrackToPlaylist(track, playlist)
    }

    override fun getTracksByIds(trackIds: List<Long>): Flow<List<Track>> {
        return playlistRepository.getTracksByIds(trackIds)
    }

    override suspend fun deleteTrackFromPlaylist(trackId: Long, playlist: Playlist) {
        playlistRepository.deleteTrackFromPlaylist(trackId, playlist)
    }

    override suspend fun sharePlaylist(playlist: Playlist, tracks: List<Track>) {
        val textMessage = buildTextMessage(playlist, tracks)
        externalNavigator.shareMessage(textMessage)
    }

    override suspend fun deletePlaylistById(playlistId: Long) {
        playlistRepository.deletePlaylistById(playlistId)
    }

    private fun buildTextMessage(playlist: Playlist, tracks: List<Track>): String {
        val builder = StringBuilder()
        builder.append(playlist.title).append("\n")
        playlist.description?.takeIf { it.isNotBlank() }?.let { builder.append(it).append("\n") }
        builder.append(
            context.resources.getQuantityString(
                R.plurals.playlist_size,
                playlist.playlistSize,
                playlist.playlistSize
            )
        ).append("\n")
        tracks.forEachIndexed { index, track ->
            val duration = timeConversion(track.trackTime)
            builder.append("${index + 1}. ${track.artistName} - ${track.trackName} ($duration)")
                .append("\n")
        }
        return builder.toString()
    }
}
