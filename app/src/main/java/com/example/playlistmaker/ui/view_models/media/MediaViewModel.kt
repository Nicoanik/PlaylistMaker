package com.example.playlistmaker.ui.view_models.media

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.media.FavoritesInteractor
import com.example.playlistmaker.domain.media.PlaylistInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MediaViewModel(
    private val favoritesInteractor: FavoritesInteractor,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private val _state = MutableStateFlow(MediaState())
    val state = _state

    init {
        getFavorites()
        getPlaylists()
    }

    private fun getFavorites() {
        viewModelScope.launch {
            favoritesInteractor
                .getFavorites()
                .collect { tracks ->
                    _state.update { it.copy(favoriteTracks = tracks) }
                }
        }
    }

    private fun getPlaylists() {
        viewModelScope.launch {
            playlistInteractor
                .getPlaylists()
                .collect { playlists ->
                    _state.update { it.copy(playlists = playlists) }
                }
        }
    }
}