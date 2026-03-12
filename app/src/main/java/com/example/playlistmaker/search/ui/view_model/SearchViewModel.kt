package com.example.playlistmaker.search.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.search.domain.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.SearchTracksInteractor
import com.example.playlistmaker.media.domain.models.Track
import com.example.playlistmaker.utils.debounce
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchTracksInteractor: SearchTracksInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor
) : ViewModel() {

    private val _state = MutableStateFlow<SearchState>(SearchState.Default)
    val state = _state

    private val showToast = SingleLiveEvent<String?>()
    fun observeShowToast(): LiveData<String?> = showToast

    private var latestSearchText: String? = null
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
        if (latestSearchText != changedText) {
            latestSearchText = changedText
            trackSearchDebounce(changedText)
        }
    }

    fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderSearchState(SearchState.Loading)
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

        if (_state.value is SearchState.Loading) {
            when {
                errorMessage != null -> {
                    renderSearchState(SearchState.Error)
                    showToast.postValue(errorMessage)
                }

                tracks.isEmpty() -> {
                    renderSearchState(SearchState.Empty)
                }

                else -> {
                    renderSearchState(SearchState.Content(tracks))
                }
            }
        }
    }

    fun getSearchHistory() {
        renderSearchState(SearchState.History(searchHistoryInteractor.getSearchHistory()))
    }

    fun addTrackToSearchHistory(track: Track) {
        searchHistoryInteractor.addTrackToSearchHistory(track)
        if (_state.value is SearchState.History) getSearchHistory()
    }

    fun clearSearchHistory() {
        searchHistoryInteractor.clearSearchHistory()
    }

    private fun renderSearchState(state: SearchState) {
        _state.value = state
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
