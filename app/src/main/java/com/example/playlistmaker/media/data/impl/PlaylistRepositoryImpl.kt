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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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

    override suspend fun addPlaylist(playlist: Playlist) {
        playlistDao.addPlaylist(playlistBdConverter.map(playlist))
    }

    override fun getPlaylists(): Flow<List<Playlist>> =
        playlistDao.getPlaylists().map { playlists ->
            convertFromPlaylistEntity(playlists)
        }

    override fun saveImageToPrivateStorage(uri: Uri) {
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myplaylists")
        if (!filePath.exists()) filePath.mkdirs()
        val file = File(filePath, "first_cover.jpg")
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        playlistTrackDao.insertTrack(playlistTrackDbConverter.map(track))
        playlistDao.addPlaylist(
            playlistBdConverter.map(
                playlist.copy(
                    trackIds = playlist.trackIds + track.trackId!!.toLong(),
                    playlistSize = playlist.playlistSize + 1
                )
            )
        )
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlist -> playlistBdConverter.map(playlist) }
    }
}