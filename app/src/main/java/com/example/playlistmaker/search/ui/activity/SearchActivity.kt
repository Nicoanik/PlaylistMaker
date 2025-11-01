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
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.player.ui.activity.PlayerActivity
import com.example.playlistmaker.search.ui.view_model.SearchState
import com.example.playlistmaker.search.ui.view_model.SearchViewModel
import com.google.gson.Gson
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue


class SearchActivity : AppCompatActivity() {

    private val gson: Gson by inject()

    private val viewModel by viewModel<SearchViewModel>()
    private lateinit var binding: ActivitySearchBinding

    private var isClickAllowed = true

    lateinit var adapterSearch: TracksAdapter
    lateinit var adapterHistory: TracksAdapter
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
                    viewModel.addTrackToSearchHistory(track)
                    mediaIntent.putExtra(TRACK_INTENT, gson.toJson(track))
                    startActivity(mediaIntent)
                }
            }
        }

        viewModel.observeState().observe(this) {
            renderSearch(it)
        }

        viewModel.observeShowToast().observe(this) {
            showToast(it.toString())
        }

        adapterSearch = TracksAdapter(onItemClickListener)
        adapterHistory = TracksAdapter(onItemClickListener)

        binding.rvTracksList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvTracksList.adapter = adapterSearch

        binding.rvSearchHistory.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvSearchHistory.adapter = adapterHistory

        binding.backButtonSearch.setOnClickListener {
            finish()
        }

        binding.clearSearchHistoryButton.setOnClickListener {
            viewModel.clearSearchHistory()
            binding.vgSearchHistory.isVisible = false
        }

        binding.etQueryInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.request(binding.etQueryInput.text.toString())
                true
            }
            false
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    viewModel.getSearchHistory()
                } else{
                    binding.vgSearchHistory.isVisible = false
                }
                binding.clearButton.isVisible = !s.isNullOrEmpty()
                viewModel.searchDebounce(s?.toString() ?: TEXT_DEF)
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        binding.etQueryInput.addTextChangedListener(textWatcher)

        binding.clearButton.setOnClickListener {
            placeholderInvisible()
            binding.etQueryInput.setText(TEXT_DEF)
            binding.rvTracksList.isVisible = false
            binding.vgSearchHistory.isVisible = (adapterHistory.tracks.isNotEmpty())
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
            viewModel.request(binding.etQueryInput.text.toString())
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

    private fun showLoading() {
        binding.apply {
            rvTracksList.isVisible = false
            tvPlaceholderMessage.isVisible = false
            progressBar.isVisible = true
        }
    }

    private fun showError() {
        binding.apply {
            progressBar.isVisible = false
            vgSearchHistory.isVisible = false
            rvTracksList.isVisible = false
            tvPlaceholderMessage.text = getString(R.string.something_went_wrong)
            tvPlaceholderMessage.isVisible = true
            ivPlaceholderErrorImage.isVisible = true
            refreshButtonSearch.isVisible = true
        }
    }

    private fun showContent(tracks: List<Track>) {
        adapterSearch.tracks.clear()
        adapterSearch.tracks.addAll(tracks)
        adapterSearch.notifyDataSetChanged()
        placeholderInvisible()
        binding.apply {
            progressBar.isVisible = false
            vgSearchHistory.isVisible = false
            rvTracksList.isVisible = true
        }
    }

    private fun showEmpty() {
        binding.apply {
            progressBar.isVisible = false
            vgSearchHistory.isVisible = false
            rvTracksList.isVisible = false
            tvPlaceholderMessage.text = getString(R.string.nothing_found)
            tvPlaceholderMessage.isVisible = true
            ivPlaceholderEmptyImage.isVisible = true
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
    private fun showHistory(tracksHistory: List<Track>) {
        if (tracksHistory.isNotEmpty()) {
            adapterHistory.tracks.clear()
            adapterHistory.tracks.addAll(tracksHistory)
            adapterHistory.notifyDataSetChanged()
            placeholderInvisible()
            binding.apply {
                progressBar.isVisible = false
                rvTracksList.isVisible = false
                vgSearchHistory.isVisible = true
            }
        }
    }

    fun renderSearch(state: SearchState) {
        when (state) {
            is SearchState.Loading -> showLoading()
            is SearchState.Content -> showContent(state.tracks)
            is SearchState.Error -> showError()
            is SearchState.Empty -> showEmpty()
            is SearchState.History -> showHistory(state.tracksHistory)
        }
    }

    companion object {
        const val TEXT_DEF = ""
        const val TRACK_INTENT = "track_intent"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}
