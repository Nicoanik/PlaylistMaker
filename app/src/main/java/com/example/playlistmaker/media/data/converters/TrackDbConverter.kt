package com.example.playlistmaker.media.data.converters

import com.example.playlistmaker.media.data.db.entity.FavoriteTrackEntity
import com.example.playlistmaker.media.domain.models.Track

class TrackDbConverter {

    fun map(track: Track): FavoriteTrackEntity {
        return FavoriteTrackEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTime,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.artworkUrl100,
            track.previewUrl,
            System.currentTimeMillis()
        )
    }

    fun map(track: FavoriteTrackEntity): Track {
        return Track(
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
