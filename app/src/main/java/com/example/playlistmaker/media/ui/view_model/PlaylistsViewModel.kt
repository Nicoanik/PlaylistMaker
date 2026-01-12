package com.example.playlistmaker.media.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.PlaylistInteractor
import com.example.playlistmaker.media.domain.models.Playlist
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private val _state = MutableLiveData<PlaylistState>()
    fun state(): LiveData<PlaylistState> = _state

    fun getPlaylists() {
        viewModelScope.launch {
            playlistInteractor
                .getPlaylists()
                .collect { playlists ->
                    postState(playlists)
                }
        }
    }

    private fun postState(playlists: List<Playlist>) {
        if (playlists.isEmpty()) {
            _state.postValue(PlaylistState.Empty)
        } else {
            _state.postValue(PlaylistState.Content(playlists))
        }
    }
}
