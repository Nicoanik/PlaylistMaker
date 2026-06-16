package com.example.playlistmaker.data.search.dto

import com.example.playlistmaker.domain.models.Track
import com.google.gson.annotations.SerializedName

data class TrackDto(
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    @SerializedName("trackTimeMillis") val trackTime: Long,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val artworkUrl100: String,
    val previewUrl: String
)

fun TrackDto.toDomain(): Track {
    return Track(
        trackId = trackId,
        trackName = trackName,
        artistName = artistName,
        trackTime = trackTime,
        collectionName = collectionName,
        releaseDate = releaseDate,
        primaryGenreName = primaryGenreName,
        country = country,
        artworkUrl100 = artworkUrl100,
        previewUrl = previewUrl
    )
}
