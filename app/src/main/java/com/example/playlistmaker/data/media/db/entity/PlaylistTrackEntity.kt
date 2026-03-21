package com.example.playlistmaker.data.media.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("playlist_tracks")
data class PlaylistTrackEntity(
    @PrimaryKey
    val trackId: Long?,
    val trackName: String?,
    val artistName: String?,
    val trackTime: Long?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val artworkUrl100: String?,
    val previewUrl: String?
)
