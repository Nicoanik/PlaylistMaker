package com.example.playlistmaker.playlist.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.PlaylistInteractor
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private val _state = MutableLiveData<PlaylistState>()
    val state: LiveData<PlaylistState> = _state

    fun getPlaylistById(playlistId: Long) {
        viewModelScope.launch {
            playlistInteractor.getPlaylistById(playlistId)
                .collect { playlist ->
                    playlistInteractor.getTracksByIds(playlist.trackIds)
                        .collect { tracks ->
                            _state.postValue(
                                PlaylistState.Content(
                                    playlist,
                                    ((tracks.sumOf { it.trackTime ?: 0 }) / 60000).toInt()
                                )
                            )
                        }
                }
        }
    }
}
