package com.example.playlistmaker.search.ui.view_model

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.search.domain.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.SearchTracksInteractor
import com.example.playlistmaker.search.domain.models.Track

class SearchViewModel(
    private val searchTracksInteractor: SearchTracksInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor
): ViewModel() {

    private val stateLiveData = MutableLiveData<SearchState>()
    fun observeState(): LiveData<SearchState> = stateLiveData

    private val showToast = SingleLiveEvent<String?>()
    fun observeShowToast(): LiveData<String?> = showToast

    private var latestSearchText: String? = null

    private val handlerMain = Handler(Looper.getMainLooper())

    init {
        getSearchHistory()
    }

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }

        this.latestSearchText = changedText
        handlerMain.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        val searchRunnable = Runnable { request(changedText) }

        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handlerMain.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime
        )
    }

   fun request(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            handlerMain.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
            renderSearchState(
                SearchState.Loading
            )
            searchTracksInteractor.searchTracks(
                newSearchText,
                object : SearchTracksInteractor.TracksConsumer {
                    override fun consume(foundTracks: List<Track>?, errorMessage: String?) {
                        handlerMain.post {
                            val tracks = mutableListOf<Track>()

                            if (foundTracks != null) {
                                tracks.addAll(foundTracks)
                            }

                            if (stateLiveData.value is SearchState.Loading) {
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
                    }
                }
            )
        }
   }

    fun getSearchHistory() {
        renderSearchState(SearchState.History(searchHistoryInteractor.getSearchHistory()))
    }

    fun addTrackToSearchHistory(track: Track) {
        searchHistoryInteractor.addTrackToSearchHistory(track)
        if (stateLiveData.value is SearchState.History) getSearchHistory()
    }

    fun clearSearchHistory() {
        searchHistoryInteractor.clearSearchHistory()
    }

    private fun renderSearchState(state: SearchState) {
        stateLiveData.postValue(state)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
    }
}
