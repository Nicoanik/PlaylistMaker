package com.example.playlistmaker.media.ui.view_model

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.PlaylistInteractor
import com.example.playlistmaker.media.domain.models.Playlist
import kotlinx.coroutines.launch

open class CreatePlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    fun createPlaylist(
        title: String,
        description: String?,
        coverUri: Uri?
    ) {
        viewModelScope.launch {
            playlistInteractor.insertPlaylist(
                Playlist(
                    title = title,
                    description = description,
                    coverPath = coverUri?.let { playlistInteractor.saveImageToPrivateStorage(it) }
                )
            )
        }
    }
}
