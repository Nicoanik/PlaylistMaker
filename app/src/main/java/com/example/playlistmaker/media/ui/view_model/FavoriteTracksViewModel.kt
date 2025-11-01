package com.example.playlistmaker.media.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FavoriteTracksViewModel: ViewModel() {

    private val stateLiveData = MutableLiveData<MediaState>()
    fun observeState(): LiveData<MediaState> = stateLiveData

    init {
        stateLiveData.postValue(MediaState.Empty)
    }
}
