package com.example.playlistmaker.search.ui.view_model

import android.util.Log
import androidx.compose.runtime.currentComposer
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.search.domain.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.SearchTracksInteractor
import com.example.playlistmaker.media.domain.models.Track
import com.example.playlistmaker.utils.debounce
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchTracksInteractor: SearchTracksInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor
) : ViewModel() {

    private val _state = MutableStateFlow(SearchState())
    val state = _state
    private val trackSearchDebounce = debounce<String>(
        SEARCH_DEBOUNCE_DELAY,
        viewModelScope,
        true
    ) { changedText ->
        searchRequest(changedText)
    }

    init {
        getSearchHistory()
    }

    fun searchDebounce(changedText: String) {
        if (state.value.searchText != changedText) {
            _state.update { current -> current.copy(searchText = changedText) }
            if (changedText == "") {
                _state.update { current -> current.copy(content = emptyList()) }
                getSearchHistory()
            }
            trackSearchDebounce(changedText)
        }
    }

    fun searchRequest(newSearchText: String) {
        Log.d("Nico", "searchRequest($newSearchText)")
        if (newSearchText.isNotEmpty()) {
            _state.update { current ->
                current.copy(
                    isLoading = true,
                    error = false,
                    empty = false
                )
            }
            viewModelScope.launch {
                searchTracksInteractor
                    .searchTracks(newSearchText)
                    .collect { pair ->
                        processResult(pair.first, pair.second)
                    }
            }
        }
    }

    private fun processResult(foundTracks: List<Track>?, errorMessage: String?) {
        val tracks = mutableListOf<Track>()

        if (foundTracks != null) {
            tracks.addAll(foundTracks)
        }

        if (state.value.isLoading) {
            _state.update { current -> current.copy(isLoading = false) }
            when {
                errorMessage != null -> {
                    _state.update { current -> current.copy(error = true, toastMessage = errorMessage) }
                }

                tracks.isEmpty() -> {
                    _state.update { current -> current.copy(empty = true) }
                }

                else -> {
                    _state.update { current -> current.copy(content = tracks) }
                }
            }
        }
    }

    private fun getSearchHistory() {
        _state.update { current -> current.copy(history = searchHistoryInteractor.getSearchHistory()) }
    }

    fun addTrackToSearchHistory(track: Track) {
        searchHistoryInteractor.addTrackToSearchHistory(track)
        getSearchHistory()
    }

    fun clearSearchHistory() {
        searchHistoryInteractor.clearSearchHistory()
        getSearchHistory()
    }

    fun toastShown() {
        _state.update { current -> current.copy(toastMessage = "") }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
