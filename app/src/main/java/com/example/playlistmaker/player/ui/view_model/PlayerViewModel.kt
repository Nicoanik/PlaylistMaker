package com.example.playlistmaker.player.ui.view_model

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.db.FavoritesInteractor
import com.example.playlistmaker.media.domain.db.PlaylistInteractor
import com.example.playlistmaker.player.ui.view_model.PlayerState.Companion.PLAYBACK_DEF
import com.example.playlistmaker.media.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale


class PlayerViewModel(
    private val track: Track,
    private val mediaPlayer: MediaPlayer,
    private val favoritesInteractor: FavoritesInteractor,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private var isFavorite = false
    private val _state = MutableLiveData<PlayerState>()
    fun state(): LiveData<PlayerState> = _state

    private var timerJob: Job? = null

    init {
        viewModelScope.launch {
            isFavorite()
            preparePlayer()
        }
    }

    fun getPlaylists() {
        viewModelScope.launch {
            playlistInteractor
                .getPlaylists()
                .collect { playlists ->
                    _state.postValue(
                        PlayerState.BottomSheetContent(
                            isFavorite,
                            getCurrentPlayerPosition(),
                            playlists
                        )
                    )
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        releasePlayer()
    }

    fun onPlayButtonClicked() {
        when (_state.value) {
            is PlayerState.Playing -> pausePlayer()
            is PlayerState.Prepared, is PlayerState.Paused -> startPlayer()
            else -> {}
        }
    }

    fun onFavoriteButton() {
        viewModelScope.launch {
            when (isFavorite) {
                true -> favoritesInteractor.deleteFromFavorites(track.trackId)
                false -> favoritesInteractor.addToFavorites(track)
            }
            isFavorite()
            when (_state.value) {
                is PlayerState.Prepared -> _state.postValue(PlayerState.Prepared(isFavorite))
                is PlayerState.Playing -> _state.postValue(
                    PlayerState.Playing(
                        isFavorite,
                        getCurrentPlayerPosition()
                    )
                )

                is PlayerState.Paused -> _state.postValue(
                    PlayerState.Paused(
                        isFavorite,
                        getCurrentPlayerPosition()
                    )
                )

                else -> {}
            }
        }
    }

    private suspend fun isFavorite() {
        isFavorite = favoritesInteractor.isTrackFavorite(track.trackId)
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            _state.postValue(PlayerState.Prepared(isFavorite))
        }
        mediaPlayer.setOnCompletionListener {
            timerJob?.cancel()
            _state.postValue(PlayerState.Prepared(isFavorite))
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        _state.postValue(PlayerState.Playing(isFavorite, getCurrentPlayerPosition()))
        startTimer()
    }

    private fun pausePlayer() {
        timerJob?.cancel()
        mediaPlayer.pause()
        _state.postValue(PlayerState.Paused(isFavorite, getCurrentPlayerPosition()))
    }

    private fun releasePlayer() {
        timerJob?.cancel()
        mediaPlayer.stop()
        mediaPlayer.release()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (mediaPlayer.isPlaying) {
                delay(DELAY_TIME_PROGRESS)
                _state.postValue(PlayerState.Playing(isFavorite, getCurrentPlayerPosition()))
            }
        }
    }

    private fun getCurrentPlayerPosition(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
            ?: PLAYBACK_DEF
    }


    companion object {
        private const val DELAY_TIME_PROGRESS = 300L
    }
}
