package com.example.playlistmaker.search.ui.view_model

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

    fun onSearchTextChanged(text: String) {
        _state.update { it.copy(searchText = text) }
        if (text.isEmpty()) {
            _state.update { it.copy(content = emptyList(), error = false, empty = false) }
            getSearchHistory()
        }
        trackSearchDebounce(text)
    }

    fun searchRequest(text: String) {
        if (text.isNotEmpty()) {
            _state.update {
                it.copy(
                    isLoading = true,
                    error = false,
                    empty = false
                )
            }
            viewModelScope.launch {
                searchTracksInteractor
                    .searchTracks(text)
                    .collect { pair ->
                        processResult(pair.first, pair.second)
                    }
            }
        }
    }

    private fun processResult(foundTracks: List<Track>?, errorMessage: String?) {
        if (state.value.isLoading) {
            _state.update {
                it.copy(
                    isLoading = false,
                    error = errorMessage != null,
                    empty = foundTracks?.isEmpty() == true,
                    content = foundTracks ?: emptyList(),
                    toastMessage = errorMessage ?: ""
                )
            }
        }
    }

    private fun getSearchHistory() {
        _state.update { it.copy(history = searchHistoryInteractor.getSearchHistory()) }
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
        _state.update { it.copy(toastMessage = "") }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        const val CLICK_DEBOUNCE_DELAY = 1000L
    }

}