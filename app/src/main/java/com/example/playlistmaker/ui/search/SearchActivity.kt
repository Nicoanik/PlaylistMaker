package com.example.playlistmaker.ui.search

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.ui.mediaplayer.MediaPlayerActivity
import com.example.playlistmaker.OnItemClickListener
import com.example.playlistmaker.R
import com.example.playlistmaker.SearchHistory
import com.example.playlistmaker.data.dto.TracksSearchResponse
import com.example.playlistmaker.data.network.ItunesApiService
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    companion object {
        const val EDIT_TEXT = "EDIT_TEXT"
        const val TEXT_DEF = ""
        const val TRACK_INTENT = "track_intent"
        const val SEARCH_TRACKS = "search_tracks"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private var editText: String = TEXT_DEF

    private var isClickAllowed = true
    private val searchRunnable = Runnable { request() }

    private val itunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val itunesService = retrofit.create(ItunesApiService::class.java)

    private lateinit var backButton: ImageView
    private lateinit var clearButton: ImageView
    private lateinit var tvPlaceholderMessage: TextView
    private lateinit var ivPlaceholderErrorImage: ImageView
    private lateinit var ivPlaceholderInternetImage: ImageView
    private lateinit var refreshButtonSearch: Button
    private lateinit var clearButtonSearchHistory: Button
    private lateinit var etQueryInput: EditText
    private lateinit var rvTracksList: RecyclerView
    private lateinit var rvSearchHistory: RecyclerView
    private lateinit var vgSearchHistory: ViewGroup
    private lateinit var progressBar: ProgressBar

    private val tracks: MutableList<Track> = mutableListOf()

    lateinit var adapterTracks: TracksAdapter
    lateinit var adapterSearches: TracksAdapter

    private val mainHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_search)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        backButton = findViewById(R.id.back_button_search)
        clearButton = findViewById(R.id.clear_button)
        tvPlaceholderMessage = findViewById(R.id.tv_placeholder_message)
        ivPlaceholderErrorImage = findViewById(R.id.iv_placeholder_error_image)
        ivPlaceholderInternetImage = findViewById(R.id.iv_placeholder_internet_image)
        refreshButtonSearch = findViewById(R.id.refresh_button_search)
        clearButtonSearchHistory = findViewById(R.id.clear_button_search_history)
        etQueryInput = findViewById(R.id.et_queryInput)
        rvTracksList = findViewById(R.id.rv_tracks_list)
        rvSearchHistory = findViewById(R.id.rv_search_history)
        vgSearchHistory = findViewById(R.id.vg_search_history)
        progressBar = findViewById(R.id.progressBar)

        val searchHistory = SearchHistory(this)

        vgSearchHistory.isVisible = (searchHistory.tracks.isNotEmpty())

        clearButtonSearchHistory.setOnClickListener {
            searchHistory.clearSearchHistory()
            vgSearchHistory.isVisible = false
        }

        val mediaIntent = Intent(this, MediaPlayerActivity::class.java)
        val onItemClickListener = object : OnItemClickListener {
            override fun onItemClick(track: Track) {
                if (clickDebounce()) {
                    searchHistory.addTrackToSearchHistory(track)
                    adapterSearches.notifyDataSetChanged()
                    mediaIntent.putExtra(TRACK_INTENT, Gson().toJson(track))
                    startActivity(mediaIntent)
                }
            }
        }

        adapterTracks = TracksAdapter(onItemClickListener)
        adapterSearches = TracksAdapter(onItemClickListener)

        adapterTracks.tracks = tracks
        rvTracksList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvTracksList.adapter = adapterTracks

        adapterSearches.tracks = searchHistory.tracks
        rvSearchHistory.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvSearchHistory.adapter = adapterSearches

        backButton.setOnClickListener {
            finish()
        }

        etQueryInput.setText(editText)

        etQueryInput.setOnEditorActionListener { _, actionId, _ ->
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
                clearButton.isVisible = !s.isNullOrEmpty()
                vgSearchHistory.isVisible = (s.isNullOrEmpty() && tracks.isEmpty() && searchHistory.tracks.isNotEmpty())
                editText = etQueryInput.text.toString()
                searchDebounce()
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        etQueryInput.addTextChangedListener(textWatcher)

        clearButton.setOnClickListener {
            etQueryInput.setText(TEXT_DEF)
            tracks.clear()
            placeholderInvisible()
            adapterTracks.notifyDataSetChanged()
            vgSearchHistory.isVisible = (searchHistory.tracks.isNotEmpty())
        }

        rvTracksList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    etQueryInput.clearFocus()
                    val imm = etQueryInput.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(etQueryInput.windowToken, 0)
                }
            }
        })

        refreshButtonSearch.setOnClickListener {
            placeholderInvisible()
            request()
        }
    }

    private fun request() {
        if (etQueryInput.text.isNotEmpty()) {
            rvTracksList.isVisible = false
            progressBar.isVisible = true
            itunesService.search(etQueryInput.text.toString()).enqueue(object :
                Callback<TracksSearchResponse> {
                override fun onResponse(call: Call<TracksSearchResponse>,
                                        response: Response<TracksSearchResponse>
                ) {
                    progressBar.isVisible = false
                    rvTracksList.isVisible = true
                    if (response.isSuccessful) {
                        val tracksJson = response.body()?.results
                        tracks.clear()
                        tracks.addAll(tracksJson!!)
                        if (tracks.isEmpty()) {
                            tvPlaceholderMessage.visibility = View.VISIBLE
                            ivPlaceholderErrorImage.visibility = View.VISIBLE
                            showMessage(getString(R.string.nothing_found), "")}
                        else {
                            adapterTracks.notifyDataSetChanged()
                        }
                    } else {
                        tvPlaceholderMessage.visibility = View.VISIBLE
                        ivPlaceholderInternetImage.visibility = View.VISIBLE
                        refreshButtonSearch.visibility = View.VISIBLE
                        showMessage(getString(R.string.something_went_wrong), response.code().toString())
                    }
                }

                override fun onFailure(call: Call<TracksSearchResponse>, t: Throwable) {
                    progressBar.isVisible = false
                    tvPlaceholderMessage.visibility = View.VISIBLE
                    ivPlaceholderInternetImage.visibility = View.VISIBLE
                    refreshButtonSearch.visibility = View.VISIBLE
                    showMessage(getString(R.string.something_went_wrong), t.message.toString())
                }

            })
        }
    }

    private fun showMessage(text: String, additionalMessage: String) {
        if (text.isNotEmpty()) {
            tracks.clear()
            adapterTracks.notifyDataSetChanged()
            tvPlaceholderMessage.text = text
            if (additionalMessage.isNotEmpty()) {
                Toast.makeText(applicationContext, additionalMessage, Toast.LENGTH_LONG)
                    .show()
            }
        } else {
            placeholderInvisible()
        }
    }

    private fun placeholderInvisible() {
        tvPlaceholderMessage.visibility = View.INVISIBLE
        ivPlaceholderErrorImage.visibility = View.INVISIBLE
        ivPlaceholderInternetImage.visibility = View.INVISIBLE
        refreshButtonSearch.visibility = View.INVISIBLE
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
            mainHandler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun searchDebounce() {
        mainHandler.removeCallbacks(searchRunnable)
        mainHandler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }
}
