package com.example.playlistmaker.media.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Playlist(
    val id: Int = 0,
    val title: String,
    val description: String?,
    val coverUri: String?,
    val trackIds: List<Long> = emptyList(),
    val playlistSize: Int = 0
) : Parcelable
