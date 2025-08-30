package com.example.playlistmaker

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.SearchActivity.Companion.TRACK_INTENT
import com.google.gson.Gson

class MediaPlayerActivity : AppCompatActivity() {

    private lateinit var backButton: ImageView
    private lateinit var ivAlbumCover: ImageView
    private lateinit var tvTrackName: TextView
    private lateinit var tvArtistName: TextView
    private lateinit var tvTrackTime: TextView
    private lateinit var tvCollectionName: TextView
    private lateinit var tvReleaseDate: TextView
    private lateinit var tvPrimaryGenreName: TextView
    private lateinit var tvCountry: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_media_player)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_media_player)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        backButton = findViewById(R.id.back_button_audio_player)
        ivAlbumCover = findViewById(R.id.iv_album_cover)
        tvTrackName = findViewById(R.id.tv_track_name)
        tvArtistName = findViewById(R.id.tv_artist_name)
        tvTrackTime = findViewById(R.id.tv_track_time)
        tvCollectionName = findViewById(R.id.tv_collection_name)
        tvReleaseDate = findViewById(R.id.tv_release_date)
        tvPrimaryGenreName = findViewById(R.id.tv_primary_genre_name)
        tvCountry = findViewById(R.id.tv_country)

        backButton.setOnClickListener {
            finish()
        }

        val mediaTrack = Gson().fromJson(intent.getStringExtra(TRACK_INTENT), Track::class.java)

        Glide.with(this)
            .load(mediaTrack.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.album_cover_placeholder)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(8, this)))
            .into(ivAlbumCover)
        tvTrackName.text = mediaTrack.trackName
        tvArtistName.text = mediaTrack.artistName
        tvTrackTime.text = timeConversion(mediaTrack.trackTime)
        tvCollectionName.text = mediaTrack.collectionName
        tvReleaseDate.text = mediaTrack.releaseDate.substring(0, 4)
        tvPrimaryGenreName.text = mediaTrack.primaryGenreName
        tvCountry.text = mediaTrack.country
    }
}
