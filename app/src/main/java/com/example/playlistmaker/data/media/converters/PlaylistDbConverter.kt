package com.example.playlistmaker.data.media.converters

import com.example.playlistmaker.data.media.db.entity.PlaylistEntity
import com.example.playlistmaker.domain.media.models.Playlist
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PlaylistDbConverter(private val gson: Gson) {

    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlist.id,
            playlist.title,
            playlist.description,
            playlist.coverPath,
            gson.toJson(playlist.trackIds),
            playlist.playlistSize,
            System.currentTimeMillis()
        )
    }

    fun map(playlist: PlaylistEntity): Playlist {
        return Playlist(
            playlist.id,
            playlist.title,
            playlist.description,
            playlist.coverUri,
            gson.fromJson(playlist.trackIdsJson, object : TypeToken<List<Long>>() {}.type),
            playlist.playlistSize
        )
    }
}
