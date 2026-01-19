package com.example.playlistmaker.media.domain.impl

import android.net.Uri
import com.example.playlistmaker.media.domain.PlaylistInteractor
import com.example.playlistmaker.media.domain.PlaylistRepository
import com.example.playlistmaker.media.domain.models.Playlist
import com.example.playlistmaker.media.domain.models.Track
import com.example.playlistmaker.sharing.ExternalNavigator
import kotlinx.coroutines.flow.Flow
import kotlin.text.append

class PlaylistInteractorImpl(
    private val playlistRepository: PlaylistRepository,
    private val externalNavigator: ExternalNavigator
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

    override fun saveImageToPrivateStorage(uri: Uri) {
        playlistRepository.saveImageToPrivateStorage(uri)
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        playlistRepository.addTrackToPlaylist(track, playlist)
    }

    override fun getTracksByIds(trackIds: List<Long>): Flow<List<Track>> {
        return playlistRepository.getTracksByIds(trackIds)
    }

    override suspend fun deleteTrackFromPlaylist(trackId: Long?, playlist: Playlist) {
        playlistRepository.deleteTrackFromPlaylist(trackId, playlist)
    }

    override suspend fun sharePlaylist(playlist: Playlist) {
        val tracks = playlistRepository.getTracksByIds(playlist.trackIds)
        val textMessage = buildTextMessage(playlist, tracks)
    }

    private fun buildTextMessage(playlist: Playlist, tracks: List<Track>) : String {
        val builder = StringBuilder()
        builder.append(playlist.title).append("\n")
        playlist.description?.takeIf { it.isNotBlank() }?.let {
            builder.append(it).append("\n")
        }
        builder.append("${tracks.size} треков").append("\n")
        tracks.forEachIndexed { index, track ->
            val duration = formatDuration(track.trackTime)
            builder.append("${index + 1}. ${track.artistName} - ${track.trackName} ($duration)").append("\n")
        }
        return builder.toString()
    }
}
