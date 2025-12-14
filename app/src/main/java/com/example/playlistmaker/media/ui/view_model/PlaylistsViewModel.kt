package com.example.playlistmaker.media.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.db.FavoritesInteractor
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.launch

class PlaylistsViewModel: ViewModel() {

    private val _state = MutableLiveData<MediaState>()
    fun state(): LiveData<MediaState> = _state

    init {
        _state.postValue(MediaState.Empty)
    }
}
