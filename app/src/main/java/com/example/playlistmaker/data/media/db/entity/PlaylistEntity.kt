package com.example.playlistmaker.data.media.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("playlists")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val title: String,
    val description: String?,
    val coverUri: String?,
    val trackIdsJson: String,
    val playlistSize: Int,
    val timestamp: Long
)
