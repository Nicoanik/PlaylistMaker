package com.example.playlistmaker.data.dto

import com.example.playlistmaker.domain.models.Track

data class TracksSearchResponse(val results: MutableList<Track>)
