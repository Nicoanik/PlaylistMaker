package com.example.playlistmaker

import com.example.playlistmaker.domain.models.Track

interface OnItemClickListener {
    fun onItemClick(track: Track)
}
