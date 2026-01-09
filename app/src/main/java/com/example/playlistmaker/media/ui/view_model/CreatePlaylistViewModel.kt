package com.example.playlistmaker.media.ui.view_model

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.db.PlaylistInteractor
import com.example.playlistmaker.media.domain.models.Playlist
import kotlinx.coroutines.launch

class CreatePlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    fun createPlaylist(
        title: String,
        description: String?,
        cover: Uri?
    ) {
        viewModelScope.launch {
            playlistInteractor.addPlaylist(Playlist(
                title = title,
                description = description,
                coverUri = cover.toString()
            ))
        }
    }
}
