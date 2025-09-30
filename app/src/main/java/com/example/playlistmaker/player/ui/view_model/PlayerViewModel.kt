package com.example.playlistmaker.player.ui.view_model

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import java.text.SimpleDateFormat
import java.util.Locale


class PlayerViewModel(private val url: String?): ViewModel() {

    private val timerLiveData = MutableLiveData(PLAYBACK_DEF)
    fun observeProgressTime(): LiveData<String> = timerLiveData
    private val playerStateLiveData = MutableLiveData<PlayerState>(PlayerState.DEFAULT)
    fun observePlayerState(): LiveData<PlayerState> = playerStateLiveData

    enum class PlayerState(val state: Int) {
        DEFAULT(0),
        PREPARED(1),
        PLAYING(2),
        PAUSED(3)
    }

    private var mediaPlayer = MediaPlayer()

    private val mainHandler = Handler(Looper.getMainLooper())

    private val timerRunnable = Runnable {
        if (playerStateLiveData.value == PlayerState.PLAYING) {
            startTimerUpdate()
        }
    }

    init {
        preparePlayer()
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer.release()
        resetTimer()
    }

    fun onPlayButtonClicked() {
        when(playerStateLiveData.value) {
            PlayerState.PLAYING -> pausePlayer()
            PlayerState.PREPARED, PlayerState.PAUSED -> startPlayer()
            else -> Unit
        }
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerStateLiveData.postValue(PlayerState.PREPARED)
        }
        mediaPlayer.setOnCompletionListener {
            playerStateLiveData.postValue(PlayerState.PREPARED)
            resetTimer()
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playerStateLiveData.postValue(PlayerState.PLAYING)
        startTimerUpdate()
    }

    private fun pausePlayer() {
        pauseTimer()
        mediaPlayer.pause()
        playerStateLiveData.postValue(PlayerState.PAUSED)
    }

    private fun startTimerUpdate() {
        timerLiveData.postValue(SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition))
        mainHandler.postDelayed(timerRunnable, 500)
    }

    private fun pauseTimer() {
        mainHandler.removeCallbacks(timerRunnable)
    }

    private fun resetTimer() {
        mainHandler.removeCallbacks(timerRunnable)
        timerLiveData.postValue(PLAYBACK_DEF)
    }

    fun onPause() {
        pausePlayer()
    }

    companion object {
        const val PLAYBACK_DEF = "00:00"

        fun getFactory(url: String?): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                PlayerViewModel(url)
            }
        }
    }
}
