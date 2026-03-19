package com.example.playlistmaker.presentation.playlist

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.media.PlaylistInteractor
import com.example.playlistmaker.domain.media.models.Playlist
import com.example.playlistmaker.presentation.media.CreatePlaylistViewModel
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
