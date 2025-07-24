package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private val itunesBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(ItunesApi::class.java)
    private lateinit var backButton: ImageView
    private lateinit var clearButton: ImageView

    private lateinit var tvPlaceholderMessage: TextView
    private lateinit var ivPlaceholderErrorImage: ImageView
    private lateinit var ivPlaceholderInternetImage: ImageView
    private lateinit var refreshButtonSearch: Button
    private lateinit var clearButtonSearchHistory: Button
    private lateinit var edQueryInput: EditText
    private lateinit var rvTracksList: RecyclerView
    private lateinit var rvSearchHistory: RecyclerView
    private lateinit var vgSearchHistory: ViewGroup

    private val tracks: MutableList<Track> = mutableListOf()

    lateinit var adapterTracks: TracksAdapter
    lateinit var adapterSearches: TracksAdapter

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
        edQueryInput = findViewById(R.id.ed_queryInput)
        rvTracksList = findViewById(R.id.rv_tracks_list)
        rvSearchHistory = findViewById(R.id.rv_search_history)
        vgSearchHistory = findViewById(R.id.vg_search_history)

        val sharedPrefs = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
        val searchHistory = SearchHistory(sharedPrefs)

        vgSearchHistory.isVisible = (searchHistory.tracks.isNotEmpty())

        clearButtonSearchHistory.setOnClickListener {
            searchHistory.clearSearchHistory()
            vgSearchHistory.visibility = View.INVISIBLE
        }

        val mediaIntent = Intent(this, MediaActivity::class.java)
        val onItemClickListener = object : OnItemClickListener {
            override fun onItemClick(track: Track) {
                searchHistory.addTrackToSearchHistory(track)
                adapterSearches.notifyDataSetChanged()
                mediaIntent.putExtra(TRACK, Gson().toJson(track))
                startActivity(mediaIntent)
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

        edQueryInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                request()
                true
            }
            false
        }
        edQueryInput.setText(editText)

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                placeholderInvisible()
                clearButton.isVisible = !s.isNullOrEmpty()
                vgSearchHistory.isVisible = (s.isNullOrEmpty() &&searchHistory.tracks.isNotEmpty())
                editText = edQueryInput.text.toString()
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        edQueryInput.addTextChangedListener(textWatcher)

        clearButton.setOnClickListener {
            edQueryInput.setText(TEXT_DEF)
            tracks.clear()
            placeholderInvisible()
            adapterTracks.notifyDataSetChanged()
            val imm = edQueryInput.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(edQueryInput.windowToken, 0)
            vgSearchHistory.isVisible = (searchHistory.tracks.isNotEmpty())
        }

        refreshButtonSearch.setOnClickListener {
            placeholderInvisible()
            request()
        }
    }

    private fun request() {
        if (edQueryInput.text.isNotEmpty()) {
            itunesService.search(edQueryInput.text.toString()).enqueue(object :
                Callback<TracksResponse> {
                override fun onResponse(call: Call<TracksResponse>,
                                        response: Response<TracksResponse>
                ) {
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

                override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
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

    private var editText: String = TEXT_DEF

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EDIT_TEXT, editText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        editText = savedInstanceState.getString(EDIT_TEXT, TEXT_DEF)
    }

    companion object {
        const val EDIT_TEXT = "EDIT_TEXT"
        const val TEXT_DEF = ""
        const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"
        const val TRACK = "TRACK"
    }
}
