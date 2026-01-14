package com.example.playlistmaker.media.data.converters

import com.example.playlistmaker.media.data.db.entity.PlaylistTrackEntity
import com.example.playlistmaker.media.domain.models.Track

class PlaylistTrackDbConvertor {

    fun map(track: Track): PlaylistTrackEntity {
        return PlaylistTrackEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTime,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.artworkUrl100,
            track.previewUrl
        )
    }
}
