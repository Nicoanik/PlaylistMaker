package com.example.playlistmaker.playlist.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.PlaylistInteractor
import com.example.playlistmaker.media.domain.models.Playlist
import com.example.playlistmaker.media.domain.models.Track
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private lateinit var playlist: Playlist
    private lateinit var tracks: List<Track>

    private val _state = MutableLiveData<PlaylistState>()
    val state: LiveData<PlaylistState> = _state

    fun getPlaylistById(playlistId: Long) {
        viewModelScope.launch {
            playlistInteractor.getPlaylistById(playlistId)
                .collect { playlist ->
                    this@PlaylistViewModel.playlist = playlist
                    playlistInteractor.getTracksByIds(playlist.trackIds)
                        .collect { tracks ->
                            this@PlaylistViewModel.tracks = tracks
                            _state.postValue(
                                PlaylistState.Content(
                                    playlist,
                                    ((tracks.sumOf { it.trackTime ?: 0 }) / 60000).toInt(),
                                    tracks
                                )
                            )
                        }
                }
        }
    }

    fun deleteTrackById(trackId: Long?) {
        viewModelScope.launch {
            playlistInteractor.deleteTrackFromPlaylist(
                trackId,
                playlist
            )
            getPlaylistById(playlist.id)
        }
    }

    fun sharePlaylist() {
        if (playlist.playlistSize > 0) {
            viewModelScope.launch {
                playlistInteractor.sharePlaylist(playlist, tracks)
            }
        } else {
            _state.postValue(PlaylistState.Share(false))
        }
    }
}
