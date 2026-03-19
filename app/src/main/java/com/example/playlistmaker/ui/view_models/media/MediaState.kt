package com.example.playlistmaker.ui.view_models.media

import com.example.playlistmaker.domain.media.models.Track

data class MediaState(
    val favoriteTracks: List<Track> = emptyList()
)