package com.example.playlistmaker.services

import com.example.playlistmaker.media.domain.models.Track
import com.example.playlistmaker.player.ui.view_model.PlayerState
import kotlinx.coroutines.flow.StateFlow

interface AudioPlayerControl {
    fun initMediaPlayer(track: Track)
    fun getMediaPlayerState(): StateFlow<PlayerState>
    fun startPlayer()
    fun pausePlayer()
}