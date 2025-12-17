package com.example.playlistmaker.player.ui.view_model

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.db.FavoritesInteractor
import com.example.playlistmaker.player.ui.view_model.PlayerState.Companion.PLAYBACK_DEF
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale


class PlayerViewModel(
    private val track: Track,
    private val mediaPlayer: MediaPlayer,
    private val favoritesInteractor: FavoritesInteractor
): ViewModel() {

    private var isFavorite = false
    private val _playerState = MutableLiveData<PlayerState>()
    fun playerState(): LiveData<PlayerState> = _playerState

    private var timerJob: Job? = null

    init {
        viewModelScope.launch {
            isFavorite()
            preparePlayer()
        }
    }

    override fun onCleared() {
        super.onCleared()
        releasePlayer()
    }

    fun onPlayButtonClicked() {
        when(_playerState.value) {
            is PlayerState.Playing -> pausePlayer()
            is PlayerState.Prepared, is PlayerState.Paused -> startPlayer()
            else -> {}
        }
    }

    fun onFavoriteButton() {
        viewModelScope.launch {
            if (isFavorite) {
                favoritesInteractor.deleteFromFavorites(track.trackId)
            } else {
                favoritesInteractor.addToFavorites(track)
            }
            isFavorite()
            changeFavorite()
        }
    }

    private fun changeFavorite() {
        when (_playerState.value) {
            is PlayerState.Default -> _playerState.postValue(PlayerState.Default(isFavorite))
            is PlayerState.Prepared -> _playerState.postValue(PlayerState.Prepared(isFavorite))
            is PlayerState.Playing -> _playerState.postValue(PlayerState.Playing(isFavorite, getCurrentPlayerPosition()))
            is PlayerState.Paused -> _playerState.postValue(PlayerState.Paused(isFavorite, getCurrentPlayerPosition()))
            else -> {}
        }
    }

    private suspend fun isFavorite() {
        isFavorite = favoritesInteractor.isTrackFavorite(track.trackId)
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            _playerState.postValue(PlayerState.Prepared(isFavorite))
        }
        mediaPlayer.setOnCompletionListener {
            _playerState.postValue(PlayerState.Prepared(isFavorite))
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        _playerState.postValue(PlayerState.Playing(isFavorite, getCurrentPlayerPosition()))
        startTimer()
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        timerJob?.cancel()
        _playerState.postValue(PlayerState.Paused(isFavorite, getCurrentPlayerPosition()))
    }

    private fun releasePlayer() {
        mediaPlayer.stop()
        mediaPlayer.release()
        _playerState.value = PlayerState.Default(isFavorite)
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (mediaPlayer.isPlaying) {
                delay(DELAY_TIME_PROGRESS)
                _playerState.postValue(PlayerState.Playing(isFavorite, getCurrentPlayerPosition()))
            }
        }
    }

    private fun getCurrentPlayerPosition(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition) ?: PLAYBACK_DEF
    }


    companion object {
        private const val DELAY_TIME_PROGRESS = 300L
    }
}
