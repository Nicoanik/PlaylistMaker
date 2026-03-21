package com.example.playlistmaker.presentation.media

import androidx.compose.runtime.Immutable
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class MediaState(
    val favoriteTracks: ImmutableList<Track> = persistentListOf(),
    val playlists: ImmutableList<Playlist> = persistentListOf()
)