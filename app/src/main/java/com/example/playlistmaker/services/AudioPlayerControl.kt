package com.example.playlistmaker.services

import com.example.playlistmaker.ui.view_models.player.PlayerState
import kotlinx.coroutines.flow.StateFlow

interface AudioPlayerControl {
    fun initMediaPlayer()
    fun getMediaPlayerState(): StateFlow<PlayerState>
    fun startPlayer()
    fun pausePlayer()
    fun startForegroundService()
    fun stopForegroundService()
}