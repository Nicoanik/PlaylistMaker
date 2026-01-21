package com.example.playlistmaker.player.ui.view_model

import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.FavoritesInteractor
import com.example.playlistmaker.media.domain.PlaylistInteractor
import com.example.playlistmaker.media.domain.models.Playlist
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

    private var playerState = STATE_DEFAULT

    private var isFavorite = false

    private val _state = MutableLiveData<PlayerState?>()
    fun state(): LiveData<PlayerState?> = _state

    private var timerJob: Job? = null

    init {
        Log.d("Nico", "Start Init{}")
        viewModelScope.launch {
            preparePlayer()
        }
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
        if (track.trackId!!.toLong() in playlist.trackIds) {
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
        releasePlayer()
    }

    fun onPlayButtonClicked() {
        when (mediaPlayer.isPlaying) {
            true -> pausePlayer()
            false -> startPlayer()
        }
    }

    fun onFavoriteButton() {
        viewModelScope.launch {
            when (isFavorite) {
                true -> favoritesInteractor.deleteFromFavorites(track.trackId)
                false -> favoritesInteractor.addToFavorites(track)
            }
            isFavorite()
        }
    }

    private suspend fun isFavorite() {
        Log.d("Nico", "Start isFavorite()")
        isFavorite = favoritesInteractor.isTrackFavorite(track.trackId)
        _state.postValue(PlayerState.IsFavorite(isFavorite))
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
            _state.postValue(PlayerState.Prepared())
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
            timerJob?.cancel()
            _state.postValue(PlayerState.Prepared())
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
        _state.postValue(PlayerState.Playing(getCurrentPlayerPosition()))
        startTimer()
    }

    private fun pausePlayer() {
        timerJob?.cancel()
        mediaPlayer.pause()
        playerState = STATE_PAUSED
        _state.postValue(PlayerState.Paused(getCurrentPlayerPosition()))
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
                _state.postValue(PlayerState.Playing(getCurrentPlayerPosition()))
            }
        }
    }

    private fun getCurrentPlayerPosition(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
            ?: PLAYBACK_DEF
    }

    fun onResume() {
        viewModelScope.launch {
            when (playerState) {
                STATE_PREPARED -> _state.postValue(PlayerState.Prepared())
                STATE_PLAYING -> _state.postValue(PlayerState.Playing(getCurrentPlayerPosition()))
                STATE_PAUSED -> _state.postValue(PlayerState.Paused(getCurrentPlayerPosition()))
            }
            isFavorite()
        }
    }

    companion object {

        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        const val PLAYBACK_DEF = "00:00"
        private const val DELAY_TIME_PROGRESS = 300L
    }
}
