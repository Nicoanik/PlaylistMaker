package com.example.playlistmaker.ui.view_models.media

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.media.FavoritesInteractor
import com.example.playlistmaker.domain.media.models.Track
import kotlinx.coroutines.launch

class FavoriteTracksViewModel(
    private val favoritesInteractor: FavoritesInteractor
) : ViewModel() {

    private val _state = MutableLiveData<FavoriteState>()
    fun state(): LiveData<FavoriteState> = _state

    fun getFavorites() {
        viewModelScope.launch {
            favoritesInteractor
                .getFavorites()
                .collect { tracks ->
                    postState(tracks)
                }
        }
    }

    private fun postState(tracks: List<Track>) {
        if (tracks.isEmpty()) {
            _state.postValue(FavoriteState.Empty)
        } else {
            _state.postValue(FavoriteState.Content(tracks))
        }
    }
}
