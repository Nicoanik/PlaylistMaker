package com.example.playlistmaker.player.ui.view_model

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.Locale


class PlayerViewModel(private val url: String?): ViewModel() {
    private val playerStateLiveData = MutableLiveData<PlayerState>()
    fun observePlayerState(): LiveData<PlayerState> = playerStateLiveData

    enum class MediaPlayerState(val state: Int) {
        DEFAULT(0),
        PREPARED(1),
        PLAYING(2),
        PAUSED(3),
        COMPLETION(4)
    }

    private var mediaPlayer = MediaPlayer()
    private var stateMediaPlayer = MediaPlayerState.DEFAULT

    private val mainHandler = Handler(Looper.getMainLooper())

    private val timerRunnable = Runnable {
        if (stateMediaPlayer == MediaPlayerState.PLAYING) {
            startTimer()
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
        when(stateMediaPlayer) {
            MediaPlayerState.PREPARED,
            MediaPlayerState.PAUSED,
            MediaPlayerState.COMPLETION -> startPlayer()
            MediaPlayerState.PLAYING -> pausePlayer()
            else -> Unit
        }
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            stateMediaPlayer = MediaPlayerState.PREPARED
            playerStateLiveData.postValue(PlayerState.Prepared(PLAYBACK_DEF))
        }
        mediaPlayer.setOnCompletionListener {
            stateMediaPlayer = MediaPlayerState.COMPLETION
            playerStateLiveData.postValue(PlayerState.Completion(PLAYBACK_DEF))
            resetTimer()
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        stateMediaPlayer = MediaPlayerState.PLAYING
        startTimer()
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        stateMediaPlayer = MediaPlayerState.PAUSED
        playerStateLiveData.postValue(PlayerState.Paused(SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)))
        pauseTimer()
    }

    private fun startTimer() {
        playerStateLiveData.postValue(PlayerState.Playing(SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)))
        mainHandler.postDelayed(timerRunnable, 500)
    }

    private fun pauseTimer() {
        mainHandler.removeCallbacks(timerRunnable)
    }

    private fun resetTimer() {
        mainHandler.removeCallbacks(timerRunnable)
    }

    companion object {
        const val PLAYBACK_DEF = "00:00"
    }
}
