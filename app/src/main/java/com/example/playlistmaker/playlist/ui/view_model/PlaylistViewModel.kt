package com.example.playlistmaker.playlist.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.PlaylistInteractor
import com.example.playlistmaker.media.domain.models.Playlist
import com.example.playlistmaker.media.domain.models.Track
import com.example.playlistmaker.media.ui.view_model.PlaylistsState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val playlistId: Long,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private val _state = MutableLiveData<PlaylistState>()
    val state: LiveData<PlaylistState> = _state

    fun loadContent() {
        viewModelScope.launch {
//            playlistInteractor.getPlaylistById(playlistId)
//                .collect { playlist ->
//                    playlistInteractor.getTracksByIds(playlist.trackIds)
//                        .collect { tracks ->
//                            _state.postValue(
//                                PlaylistState.Content(
//                                    playlist,
//                                    ((tracks.sumOf { it.trackTime ?: 0 }) / 60000).toInt(),
//                                    tracks
//                                )
//                            )
//                        }
//                }
            val playlist = getPlaylist(playlistId)
            val tracks = getTracks(playlist.trackIds)
            _state.postValue(
                PlaylistState.Content(
                    playlist,
                    ((tracks.sumOf { it.trackTime ?: 0 }) / 60000).toInt(),
                    tracks
                )
            )
        }
    }

    private suspend fun getPlaylist(playlistId: Long): Playlist {
        return playlistInteractor.getPlaylistById(playlistId).first()
    }

    private suspend fun getTracks(trackIds: List<Long>): List<Track> {
        return playlistInteractor.getTracksByIds(trackIds).first()
    }

    fun deleteTrackById(trackId: Long) {
        viewModelScope.launch {
            val playlist = getPlaylist(playlistId)
            playlistInteractor.deleteTrackFromPlaylist(
                trackId,
                playlist
            )
            loadContent()
        }
    }

    fun sharePlaylist() {
        viewModelScope.launch {
            val playlist = getPlaylist(playlistId)
            val tracks = getTracks(playlist.trackIds)
            if (tracks.isNotEmpty()) {
                playlistInteractor.sharePlaylist(playlist, tracks)
            } else {
                _state.postValue(PlaylistState.Share(false))
            }
        }
    }

    fun deletePlaylist() {
        viewModelScope.launch {
            playlistInteractor.deletePlaylistById(playlistId)
        }
    }
}
