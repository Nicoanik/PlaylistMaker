package com.example.playlistmaker.media.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.db.FavoritesInteractor
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.launch

class FavoriteTracksViewModel(
    private val favoritesInteractor: FavoritesInteractor
): ViewModel() {

    private val _state = MutableLiveData<FavoriteState>()
    fun state(): LiveData<FavoriteState> = _state

    init { getFavorites() }

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
        if(tracks.isEmpty()) {
            _state.postValue(FavoriteState.Empty)
        } else {
            _state.postValue(FavoriteState.Content(tracks))
        }
    }
}
