package com.example.playlistmaker.media.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlaylistsViewModel : ViewModel() {

    private val _state = MutableLiveData<PlaylistState>()
    fun state(): LiveData<PlaylistState> = _state

    init {
        _state.postValue(PlaylistState.Empty)
    }
}
