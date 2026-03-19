package com.example.playlistmaker.presentation.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.media.FavoritesInteractor
import com.example.playlistmaker.domain.media.PlaylistInteractor
import com.example.playlistmaker.domain.media.models.Playlist
import com.example.playlistmaker.domain.media.models.Track
import com.example.playlistmaker.services.AudioPlayerControl
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import kotlinx.coroutines.launch


class PlayerViewModel(
    private val track: Track,
    private val favoritesInteractor: FavoritesInteractor,
    private val playlistInteractor: PlaylistInteractor,
    private val analytics: FirebaseAnalytics
) : ViewModel() {
    private var audioPlayerControl: AudioPlayerControl? = null

    private var isFavorite = false

    private val _state = MutableLiveData<PlayerState>()
    val state: LiveData<PlayerState> = _state

    fun setAudioPlayerControl(audioPlayerControl: AudioPlayerControl) {
        this.audioPlayerControl = audioPlayerControl

        viewModelScope.launch {
            audioPlayerControl.initMediaPlayer()
            audioPlayerControl.getMediaPlayerState().collect {
                _state.postValue(it)
            }
        }
    }

    fun removeAudioPlayerControl() {
        audioPlayerControl = null
    }

    fun startForegroundService() {
        if (audioPlayerControl?.getMediaPlayerState()?.value is PlayerState.Playing) {
            audioPlayerControl?.startForegroundService()
        }
    }

    fun stopForegroundService() {
        audioPlayerControl?.stopForegroundService()
    }

    fun getPlaylists() {
        viewModelScope.launch {
            playlistInteractor
                .getPlaylists()
                .collect { playlists ->
                    _state.postValue(PlayerState.BottomSheetContent(playlists))
                }
        }
    }

    fun addTrackToPlaylist(playlist: Playlist) {
        if (track.trackId in playlist.trackIds) {
            _state.postValue(PlayerState.InPlaylist(false, playlist.title))
        } else {
            viewModelScope.launch {
                playlistInteractor.addTrackToPlaylist(track, playlist)
                _state.postValue(PlayerState.InPlaylist(true, playlist.title))
            }
        }

    }

    override fun onCleared() {
        super.onCleared()
        audioPlayerControl = null
    }

    fun onPlayButtonClicked() {
        when (audioPlayerControl?.getMediaPlayerState()?.value is PlayerState.Playing) {
            true -> audioPlayerControl?.pausePlayer()
            false -> audioPlayerControl?.startPlayer()
        }
    }

    fun onFavoriteButton() {
        viewModelScope.launch {
            when (isFavorite) {
                true -> favoritesInteractor.deleteFromFavorites(track.trackId)
                false -> {
                    favoritesInteractor.addToFavorites(track)
                    analytics.logEvent("Added To Favorite") {
                        param("Track: ", track.trackName.toString())
                    }
                }
            }
            isFavorite()
        }
    }

    private suspend fun isFavorite() {
        isFavorite = favoritesInteractor.isTrackFavorite(track.trackId)
        _state.postValue(PlayerState.IsFavorite(isFavorite))
    }

    fun onResume() {
        viewModelScope.launch {
            audioPlayerControl?.getMediaPlayerState()?.collect {
                _state.postValue(it)
            }
            isFavorite()
        }
    }

}