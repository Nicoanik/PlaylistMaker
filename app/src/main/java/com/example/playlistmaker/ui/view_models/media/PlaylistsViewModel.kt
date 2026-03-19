package com.example.playlistmaker.ui.view_models.media

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.media.PlaylistInteractor
import com.example.playlistmaker.domain.media.models.Playlist
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private val _state = MutableLiveData<PlaylistsState>()
    val state: LiveData<PlaylistsState> = _state

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
            _state.postValue(PlaylistsState.Empty)
        } else {
            _state.postValue(PlaylistsState.Content(playlists))
        }
    }
}
