package com.example.playlistmaker.playlist.ui.view_model

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.PlaylistInteractor
import com.example.playlistmaker.media.domain.models.Playlist
import com.example.playlistmaker.media.ui.view_model.CreatePlaylistViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class EditPlaylistViewModel(
    private val playlistId: Long,
    private val playlistInteractor: PlaylistInteractor
) : CreatePlaylistViewModel(playlistInteractor) {

    private val _state = MutableLiveData<EditPlaylistState>()
    val state: LiveData<EditPlaylistState> = _state

    init {
        viewModelScope.launch {
            playlistInteractor.getPlaylistById(playlistId)
                .collect { playlist ->
                    _state.postValue(EditPlaylistState.Content(playlist))
                }
        }
    }

    fun updatePlaylist(
        title: String,
        description: String?,
        coverUri: Uri?,
    ) {
        viewModelScope.launch {
            val playlist = getPlaylist(playlistId)
            insertPlaylist(
                Playlist(
                    id = playlistId,
                    title = title,
                    description = description,
                    coverPath = coverUri?.let {
                        playlistInteractor.saveImageToPrivateStorage(
                            coverUri
                        )
                    } ?: playlist.coverPath,
                    trackIds = playlist.trackIds,
                    playlistSize = playlist.playlistSize
                )
            )
            _state.postValue(EditPlaylistState.Done())
        }
    }

    private suspend fun getPlaylist(id: Long): Playlist {
        return playlistInteractor.getPlaylistById(id).first()
    }

    private suspend fun insertPlaylist(playlist: Playlist) {
        playlistInteractor.insertPlaylist(playlist)
    }
}
