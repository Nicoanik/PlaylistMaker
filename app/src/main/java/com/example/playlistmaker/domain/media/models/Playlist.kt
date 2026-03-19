package com.example.playlistmaker.domain.media.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Playlist(
    val id: Long = 0,
    val title: String,
    val description: String?,
    val coverPath: String?,
    val trackIds: List<Long> = emptyList(),
    val playlistSize: Int = 0
) : Parcelable
