package com.example.playlistmaker.search.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.search.domain.SearchTracksInteractor
import com.example.playlistmaker.search.domain.models.Resource
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.player.ui.activity.PlayerActivity
import com.example.playlistmaker.search.ui.view_model.SearchViewModel
import com.google.gson.Gson
import kotlin.collections.addAll

class SearchActivity : AppCompatActivity() {

    private var viewModel: SearchViewModel? = null
    private lateinit var binding: ActivitySearchBinding

    private val tracks: MutableList<Track> = mutableListOf()
    private var editText: String = TEXT_DEF

    private var isClickAllowed = true
    private val searchRunnable = Runnable { request() }

    lateinit var adapterTracks: TracksAdapter
    lateinit var adapterSearches: TracksAdapter

    private val tracksInteractor = Creator.provideTracksInteractor()
    private val searchHistoryInteractor = Creator.provideSearchHistoryInteractor()
    private val handlerMain = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_search)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.vgSearchHistory.isVisible = (searchHistoryInteractor.getSearchHistory().isNotEmpty())

        binding.clearButtonSearchHistory.setOnClickListener {
            searchHistoryInteractor.clearSearchHistory()
            binding.vgSearchHistory.isVisible = false
        }

        val mediaIntent = Intent(this, PlayerActivity::class.java)
        val onItemClickListener = object : OnItemClickListener {
            override fun onItemClick(track: Track) {
                if (clickDebounce()) {
                    searchHistoryInteractor.addTrackToSearchHistory(track)
                    adapterSearches.notifyDataSetChanged()
                    mediaIntent.putExtra(TRACK_INTENT, Gson().toJson(track))
                    startActivity(mediaIntent)
                }
            }
        }

        adapterTracks = TracksAdapter(onItemClickListener)
        adapterSearches = TracksAdapter(onItemClickListener)

        adapterTracks.tracks = tracks
        binding.rvTracksList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvTracksList.adapter = adapterTracks

        adapterSearches.tracks = searchHistoryInteractor.getSearchHistory()
        binding.rvSearchHistory.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvSearchHistory.adapter = adapterSearches

        binding.backButtonSearch.setOnClickListener {
            finish()
        }

        binding.etQueryInput.setText(editText)

        binding.etQueryInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                request()
                true
            }
            false
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    tracks.clear()
                    adapterTracks.notifyDataSetChanged()
                }
                placeholderInvisible()
                binding.clearButton.isVisible = !s.isNullOrEmpty()
                binding.vgSearchHistory.isVisible = (s.isNullOrEmpty() && tracks.isEmpty() && searchHistoryInteractor.getSearchHistory().isNotEmpty())
                editText = binding.etQueryInput.text.toString()
                searchDebounce()
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        binding.etQueryInput.addTextChangedListener(textWatcher)

        binding.clearButton.setOnClickListener {
            binding.etQueryInput.setText(TEXT_DEF)
            tracks.clear()
            placeholderInvisible()
            adapterTracks.notifyDataSetChanged()
            binding.vgSearchHistory.isVisible = (searchHistoryInteractor.getSearchHistory().isNotEmpty())
        }

        binding.rvTracksList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    binding.etQueryInput.clearFocus()
                    val imm = binding.etQueryInput.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(binding.etQueryInput.windowToken, 0)
                }
            }
        })

        binding.refreshButtonSearch.setOnClickListener {
            placeholderInvisible()
            request()
        }
    }

    private fun request() {
        if (binding.etQueryInput.text.isNotEmpty()) {
            binding.rvTracksList.isVisible = false
            binding.progressBar.isVisible = true
            tracksInteractor.searchTracks(
                binding.etQueryInput.text.toString(),
                object : SearchTracksInteractor.TracksConsumer {
                    override fun consume(foundTracks: Resource<List<Track>>) {
                        handlerMain.post {
                            binding.progressBar.isVisible = false
                            binding.rvTracksList.isVisible = true
                            if (foundTracks is Resource.Error) {
                                binding.apply {
                                    progressBar.isVisible = false
                                    tvPlaceholderMessage.isVisible = true
                                    ivPlaceholderInternetImage.isVisible = true
                                    refreshButtonSearch.isVisible = true
                                }
                                showMessage(getString(R.string.something_went_wrong), foundTracks.message)
                            } else if (foundTracks is Resource.Success) {
                                tracks.clear()
                                tracks.addAll(foundTracks.data)
                                if (tracks.isEmpty()) {
                                    binding.tvPlaceholderMessage.isVisible = true
                                    binding.ivPlaceholderErrorImage.isVisible = true
                                    showMessage(getString(R.string.nothing_found), "")}
                                else {
                                    adapterTracks.notifyDataSetChanged()
                                }
                            } else {
                                binding.apply {
                                    tvPlaceholderMessage.isVisible = true
                                    ivPlaceholderInternetImage.isVisible = true
                                    refreshButtonSearch.isVisible = true
                                }
                                showMessage(getString(R.string.something_went_wrong), "")
                            }
                        }
                    }
                }
            )
        }
    }

    private fun showMessage(text: String, additionalMessage: String) {
        if (text.isNotEmpty()) {
            tracks.clear()
            adapterTracks.notifyDataSetChanged()
            binding.tvPlaceholderMessage.text = text
            if (additionalMessage.isNotEmpty()) {
                Toast.makeText(applicationContext, additionalMessage, Toast.LENGTH_LONG)
                    .show()
            }
        } else {
            placeholderInvisible()
        }
    }

    private fun placeholderInvisible() {
        binding.apply {
            tvPlaceholderMessage.isVisible = false
            ivPlaceholderErrorImage.isVisible = false
            ivPlaceholderInternetImage.isVisible = false
            refreshButtonSearch.isVisible = false
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EDIT_TEXT, editText)
        outState.putString(SEARCH_TRACKS, Gson().toJson(tracks))
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        editText = savedInstanceState.getString(EDIT_TEXT, TEXT_DEF)
        val json = savedInstanceState.getString(SEARCH_TRACKS, null)
        tracks.addAll((Gson().fromJson(json, Array<Track>::class.java)))
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handlerMain.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun searchDebounce() {
        handlerMain.removeCallbacks(searchRunnable)
        handlerMain.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    companion object {
        const val EDIT_TEXT = "EDIT_TEXT"
        const val TEXT_DEF = ""
        const val TRACK_INTENT = "track_intent"
        const val SEARCH_TRACKS = "search_tracks"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
