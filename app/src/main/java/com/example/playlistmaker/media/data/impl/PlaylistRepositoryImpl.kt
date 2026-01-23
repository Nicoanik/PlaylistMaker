package com.example.playlistmaker.media.data.impl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import com.example.playlistmaker.media.data.converters.PlaylistDbConverter
import com.example.playlistmaker.media.data.converters.PlaylistTrackDbConvertor
import com.example.playlistmaker.media.data.db.dao.PlaylistDao
import com.example.playlistmaker.media.data.db.dao.PlaylistTrackDao
import com.example.playlistmaker.media.data.db.entity.PlaylistEntity
import com.example.playlistmaker.media.domain.PlaylistRepository
import com.example.playlistmaker.media.domain.models.Playlist
import com.example.playlistmaker.media.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.any
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import kotlin.collections.map

class PlaylistRepositoryImpl(
    private val context: Context,
    private val playlistDao: PlaylistDao,
    private val playlistTrackDao: PlaylistTrackDao,
    private val playlistBdConverter: PlaylistDbConverter,
    private val playlistTrackDbConverter: PlaylistTrackDbConvertor
) : PlaylistRepository {

    override suspend fun insertPlaylist(playlist: Playlist) {
        playlistDao.insertPlaylist(playlistBdConverter.map(playlist))
    }

    override fun getPlaylists(): Flow<List<Playlist>> =
        playlistDao.getPlaylists().map { playlists ->
            convertFromPlaylistEntity(playlists)
        }

    override fun getPlaylistById(playlistId: Long): Flow<Playlist> {
        return playlistDao.getPlaylistById(playlistId).map { playlist ->
            playlistBdConverter.map(playlist)
        }
    }

    override fun saveImageToPrivateStorage(uri: Uri) {
        val filePath =
            File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "playlists")
        if (!filePath.exists()) filePath.mkdirs()
        val file = File(filePath, "cover.jpg")
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        playlistTrackDao.insertTrack(playlistTrackDbConverter.map(track))
        playlistDao.insertPlaylist(
            playlistBdConverter.map(
                playlist.copy(
                    trackIds = playlist.trackIds + track.trackId!!.toLong(),
                    playlistSize = playlist.playlistSize + 1
                )
            )
        )
    }

    override fun getTracksByIds(trackIds: List<Long>): Flow<List<Track>> {
        return playlistTrackDao.getTracksByIds(trackIds).map { entities ->
            entities.map { entity ->
                playlistTrackDbConverter.map(entity)
            }
        }
    }

    override suspend fun deleteTrackFromPlaylist(trackId: Long, playlist: Playlist) {
        playlistDao.insertPlaylist(
            playlistBdConverter.map(
                playlist.copy(
                    trackIds = playlist.trackIds.filter { it != trackId },
                    playlistSize = playlist.playlistSize - 1
                )
            )
        )
        val playlists = playlistDao.getPlaylists().first().map { playlistBdConverter.map(it) }
        val isPresent = playlists.any { it.trackIds.contains(trackId) }
        if (!isPresent) playlistTrackDao.deleteTrackById(trackId)
    }

    override suspend fun deletePlaylistById(playlistId: Long) {
        withContext(Dispatchers.IO) {
            val playlist = playlistDao.getPlaylistById(playlistId).first().let { playlist ->
                playlistBdConverter.map(playlist)
            }
            playlistDao.deletePlaylistById(playlistId)
            val playlists = playlistDao.getPlaylists().first().map { playlistBdConverter.map(it) }
            playlist.trackIds.forEach { trackId ->
                val isPresent = playlists.any { playlist -> playlist.trackIds.contains(trackId) }
                if (!isPresent) {
                    playlistTrackDao.deleteTrackById(trackId)
                }
            }
        }
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlist -> playlistBdConverter.map(playlist) }
    }
}
