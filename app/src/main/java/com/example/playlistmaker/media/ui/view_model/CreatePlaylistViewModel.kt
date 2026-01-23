package com.example.playlistmaker.media.ui.view_model

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.PlaylistInteractor
import com.example.playlistmaker.media.domain.models.Playlist
import com.example.playlistmaker.media.domain.models.Track
import kotlinx.coroutines.launch

open class CreatePlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    fun saveImageToPrivateStorage(uri: Uri) {
        playlistInteractor.saveImageToPrivateStorage(uri)
    }

    fun createPlaylist(
        title: String,
        description: String?,
        cover: Uri?
    ) {
        viewModelScope.launch {
            playlistInteractor.insertPlaylist(
                Playlist(
                    title = title,
                    description = description,
                    coverUri = cover.toString()
                )
            )
        }
    }
}
