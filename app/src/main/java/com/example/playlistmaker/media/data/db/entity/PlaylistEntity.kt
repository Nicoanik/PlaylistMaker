package com.example.playlistmaker.media.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("playlists")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val description: String?,
    val coverUri: String?,
    val trackIdsJson: String,
    val playlistSize: Long,
    val timestamp: Long
)
