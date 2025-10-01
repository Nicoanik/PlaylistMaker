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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.player.ui.activity.PlayerActivity
import com.example.playlistmaker.search.ui.view_model.HistoryState
import com.example.playlistmaker.search.ui.view_model.SearchState
import com.example.playlistmaker.search.ui.view_model.SearchViewModel
import com.google.gson.Gson

class SearchActivity : AppCompatActivity() {

    private var viewModel: SearchViewModel? = null
    private lateinit var binding: ActivitySearchBinding

    private var isClickAllowed = true

    lateinit var adapterTracks: TracksAdapter
    lateinit var adapterSearches: TracksAdapter
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

        val mediaIntent = Intent(this, PlayerActivity::class.java)
        val onItemClickListener = object : OnItemClickListener {
            override fun onItemClick(track: Track) {
                if (clickDebounce()) {
                    viewModel?.addTrackToSearchHistory(track)
                    mediaIntent.putExtra(TRACK_INTENT, Gson().toJson(track))
                    startActivity(mediaIntent)
                }
            }
        }

        viewModel = ViewModelProvider(this, SearchViewModel.getFactory())[SearchViewModel::class.java]
        viewModel?.observeState()?.observe(this) {
            renderSearch(it)
        }

        viewModel?.observeShowToast()?.observe(this) {
            showToast(it.toString())
        }

        viewModel?.observeSearchHistory()?.observe(this) {
            renderHistory(it)
        }

        adapterTracks = TracksAdapter(onItemClickListener)
        adapterSearches = TracksAdapter(onItemClickListener)

        binding.rvTracksList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvTracksList.adapter = adapterTracks

        viewModel?.getSearchHistory()

        binding.rvSearchHistory.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvSearchHistory.adapter = adapterSearches

        binding.backButtonSearch.setOnClickListener {
            finish()
        }

        binding.clearButtonSearchHistory.setOnClickListener {
            adapterSearches.tracks.clear()
            viewModel?.clearSearchHistory()
            binding.vgSearchHistory.isVisible = false
        }

        binding.etQueryInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel?.request(binding.etQueryInput.text.toString())
                true
            }
            false
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    adapterTracks.tracks.clear()
                    adapterTracks.notifyDataSetChanged()
                }
                placeholderInvisible()
                binding.clearButton.isVisible = !s.isNullOrEmpty()
                binding.vgSearchHistory.isVisible = (s.isNullOrEmpty() && adapterTracks.tracks.isEmpty() && adapterSearches.tracks.isNotEmpty())
                viewModel?.searchDebounce(s?.toString() ?: TEXT_DEF)
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        binding.etQueryInput.addTextChangedListener(textWatcher)

        binding.clearButton.setOnClickListener {
            placeholderInvisible()
            binding.etQueryInput.setText(TEXT_DEF)
            adapterTracks.tracks.clear()
            adapterTracks.notifyDataSetChanged()
            binding.vgSearchHistory.isVisible = (adapterSearches.tracks.isNotEmpty())
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
            viewModel?.request(binding.etQueryInput.text.toString())
        }
    }
    private fun placeholderInvisible() {
        binding.apply {
            tvPlaceholderMessage.isVisible = false
            ivPlaceholderErrorImage.isVisible = false
            ivPlaceholderEmptyImage.isVisible = false
            refreshButtonSearch.isVisible = false
        }
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handlerMain.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    fun showLoading() {
        binding.apply {
            rvTracksList.isVisible = false
            tvPlaceholderMessage.isVisible = false
            progressBar.isVisible = true
        }
    }

    fun showError(errorMessage: String) {
        binding.apply {
            rvTracksList.isVisible = false
            tvPlaceholderMessage.isVisible = true
            ivPlaceholderErrorImage.isVisible = true
            progressBar.isVisible = false
            tvPlaceholderMessage.text = errorMessage
            refreshButtonSearch.isVisible = true
        }
    }

    fun showContent(tracks: List<Track>) {
        binding.apply {
            rvTracksList.isVisible = true
            tvPlaceholderMessage.isVisible = false
            progressBar.isVisible = false
        }
        adapterTracks.tracks.addAll(tracks)
        adapterTracks.notifyDataSetChanged()
    }

    fun showEmpty(emptyMessage: String) {
        binding.apply {
            rvTracksList.isVisible = false
            tvPlaceholderMessage.isVisible = true
            ivPlaceholderEmptyImage.isVisible = true
            progressBar.isVisible = false
            tvPlaceholderMessage.text = emptyMessage
        }
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    fun renderHistory(state: HistoryState) {
        when (state) {
            is HistoryState.GetHistory -> {
                if (state.trackHistory.isNotEmpty()) {
                    adapterSearches.tracks.addAll(state.trackHistory)
                    adapterSearches.notifyDataSetChanged()
                    binding.vgSearchHistory.isVisible = true
                }
            }
            is HistoryState.AddHistory -> {
                adapterSearches.tracks.clear()
                adapterSearches.tracks.addAll(state.tracksHistory)
                adapterSearches.notifyDataSetChanged()
            }
        }
    }



    fun renderSearch(state: SearchState) {
        when (state) {
            is SearchState.Loading -> showLoading()
            is SearchState.Content -> showContent(state.tracks)
            is SearchState.Error -> showError(state.errorMessage)
            is SearchState.Empty -> showEmpty(state.message)
        }
    }

    companion object {
        const val TEXT_DEF = ""
        const val TRACK_INTENT = "track_intent"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}
