package com.example.playlistmaker.presentation.media

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.media.PlaylistInteractor
import com.example.playlistmaker.domain.media.models.Playlist
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
