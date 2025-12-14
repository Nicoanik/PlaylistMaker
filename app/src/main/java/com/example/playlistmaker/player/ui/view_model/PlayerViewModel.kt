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
    private val _playerState = MutableLiveData<PlayerState>()
    fun playerState(): LiveData<PlayerState> = _playerState

    private val _favouriteState = MutableLiveData<Boolean>()
    fun favouriteState(): LiveData<Boolean> = _favouriteState

    private var timerJob: Job? = null

    init {
        preparePlayer()
        viewModelScope.launch { isFavorite() }
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
            if (_favouriteState.value == true) {
                favoritesInteractor.deleteFromFavorites(track.trackId)
            } else {
                favoritesInteractor.addToFavorites(track)
            }
            isFavorite()
        }
    }

    private suspend fun isFavorite() {
        _favouriteState.postValue(favoritesInteractor.isTrackFavorite(track.trackId))
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            _playerState.postValue(PlayerState.Prepared())
        }
        mediaPlayer.setOnCompletionListener {
            _playerState.postValue(PlayerState.Prepared())
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        _playerState.postValue(PlayerState.Playing(getCurrentPlayerPosition()))
        startTimer()
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        timerJob?.cancel()
        _playerState.postValue(PlayerState.Paused(getCurrentPlayerPosition()))
    }

    private fun releasePlayer() {
        mediaPlayer.stop()
        mediaPlayer.release()
        _playerState.value = PlayerState.Default()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (mediaPlayer.isPlaying) {
                delay(DELAY_TIME_PROGRESS)
                _playerState.postValue(PlayerState.Playing(getCurrentPlayerPosition()))
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
