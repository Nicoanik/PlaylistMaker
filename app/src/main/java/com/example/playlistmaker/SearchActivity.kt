package com.example.playlistmaker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
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

    private lateinit var placeholderMessage: TextView
    private lateinit var queryInput: EditText
    private lateinit var tracksList: RecyclerView

    private val tracks = ArrayList<Track>()

    private val adapter = TracksAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        placeholderMessage = findViewById(R.id.placeholderMessage)
        queryInput = findViewById(R.id.queryInput)
        tracksList = findViewById(R.id.tracks_list)

        adapter.tracks = tracks

        tracksList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        tracksList.adapter = adapter

//        queryInput.setOnEditorActionListener { _, actionId, _ ->
//            if (actionId == EditorInfo.IME_ACTION_DONE) {
//                // ВЫПОЛНЯЙТЕ ПОИСКОВЫЙ ЗАПРОС ЗДЕСЬ
//                if (queryInput.text.isNotEmpty()) {
//                    itunesService.search(queryInput.text.toString()).enqueue(object :
//                        Callback<TracksResponse> {
//                        override fun onResponse(call: Call<TracksResponse>,
//                                                response: Response<TracksResponse>
//                        ) {
//                            if (response.code() == 200) {
//                                tracks.clear()
//                                if (response.body()?.results?.isNotEmpty() == true) {
//                                    tracks.addAll(response.body()?.results!!)
//                                    adapter.notifyDataSetChanged()
//                                }
//                                if (tracks.isEmpty()) {
//                                    showMessage(getString(R.string.nothing_found), "")
//                                } else {
//                                    showMessage("", "")
//                                }
//                            } else {
//                                showMessage(getString(R.string.something_went_wrong), response.code().toString())
//                            }
//                        }
//
//                        override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
//                            showMessage(getString(R.string.something_went_wrong), t.message.toString())
//                        }
//
//                    })
//                }
//                true
//            }
//            false
//        }

//        val inputEditText = findViewById<EditText>(R.id.inputEditText)
//        inputEditText.setText(editText)
        queryInput.setText(editText)

        val backButton = findViewById<ImageView>(R.id.back_button_search)
        backButton.setOnClickListener {
            finish()
        }

        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        clearButton.setOnClickListener {
            queryInput.setText(TEXT_DEF)
            val imm = queryInput.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(queryInput.windowToken, 0)
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.isVisible = !s.isNullOrEmpty()
                editText = queryInput.text.toString()
            }

            override fun afterTextChanged(s: Editable?) {}
        }
        queryInput.addTextChangedListener(textWatcher)

//        val recycler = findViewById<RecyclerView>(R.id.track_list)

        val tracks = listOf(
            Track("Smells Like Teen Spirit",
                "Nirvana",
                "5:01",
                "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg"),
            Track("Billie Jean",
                "Michael Jackson",
                "4:35",
                "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg"),
            Track("Stayin' Alive",
                "Bee Gees",
                "4:10",
                "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg"),
            Track("Whole Lotta Love",
                "Led Zeppelin",
                "5:33",
                "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg"),
            Track("Sweet Child O'Mine",
                "Guns N' Roses",
                "5:03",
                "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg")
        )

//        recycler.layoutManager = LinearLayoutManager(this)
//        recycler.adapter = TrackAdapter(tracks)
    }

//    private fun showMessage(text: String, additionalMessage: String) {
//        if (text.isNotEmpty()) {
//            placeholderMessage.visibility = View.VISIBLE
//            tracks.clear()
//            adapter.notifyDataSetChanged()
//            placeholderMessage.text = text
//            if (additionalMessage.isNotEmpty()) {
//                Toast.makeText(applicationContext, additionalMessage, Toast.LENGTH_LONG)
//                    .show()
//            }
//        } else {
//            placeholderMessage.visibility = View.GONE
//        }
//    }

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
    }
}