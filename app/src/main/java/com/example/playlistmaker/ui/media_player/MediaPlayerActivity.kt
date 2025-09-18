package com.example.playlistmaker.ui.media_player

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.search.SearchActivity.Companion.TRACK_INTENT
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.models.dpToPx
import com.example.playlistmaker.domain.models.timeConversion
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class MediaPlayerActivity : AppCompatActivity() {

    enum class PlayerState(val state: Int) {
        DEFAULT(0),
        PREPARED(1),
        PLAYING(2),
        PAUSED(3)
    }

    private var mediaPlayer = MediaPlayer()
    private var playerState = PlayerState.DEFAULT

    private lateinit var backButton: ImageView
    private lateinit var playButton: ImageButton
    private lateinit var playbackProgress: TextView
    private lateinit var ivAlbumCover: ImageView
    private lateinit var tvTrackName: TextView
    private lateinit var tvArtistName: TextView
    private lateinit var tvTrackTime: TextView
    private lateinit var tvCollectionName: TextView
    private lateinit var tvReleaseDate: TextView
    private lateinit var tvPrimaryGenreName: TextView
    private lateinit var tvCountry: TextView

    private val mainHandler = Handler(Looper.getMainLooper())

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
        playButton = findViewById(R.id.play_button)
        playbackProgress = findViewById(R.id.playback_progress)
        ivAlbumCover = findViewById(R.id.iv_album_cover)
        tvTrackName = findViewById(R.id.tv_track_name)
        tvArtistName = findViewById(R.id.tv_artist_name)
        tvTrackTime = findViewById(R.id.tv_track_time)
        tvCollectionName = findViewById(R.id.tv_collection_name)
        tvReleaseDate = findViewById(R.id.tv_release_date)
        tvPrimaryGenreName = findViewById(R.id.tv_primary_genre_name)
        tvCountry = findViewById(R.id.tv_country)

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
        playbackProgress.text = PLAYBACK_DEF

        backButton.setOnClickListener {
            finish()
        }

        preparePlayer(mediaTrack)

        playButton.setOnClickListener {
            playbackControl()
        }
    }

    private fun preparePlayer(mediaTrack: Track) {
        mediaPlayer.setDataSource(mediaTrack.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = PlayerState.PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playButton.setImageResource(R.drawable.play_button_100)
            playerState = PlayerState.PREPARED
            mainHandler.removeCallbacksAndMessages(null)
            playbackProgress.text = PLAYBACK_DEF
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playButton.setImageResource(R.drawable.pause_button_100)
        playerState = PlayerState.PLAYING
        mainHandler.post(
            object : Runnable {
                override fun run() {
                    playbackProgress.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
                    mainHandler.postDelayed(this, 500)
                }
            }
        )
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playButton.setImageResource(R.drawable.play_button_100)
        playerState = PlayerState.PAUSED
        mainHandler.removeCallbacksAndMessages(null)
    }

    private fun playbackControl() {
        when(playerState) {
            PlayerState.PLAYING -> {
                pausePlayer()
            }
            PlayerState.PREPARED, PlayerState.PAUSED -> {
                startPlayer()
            }
            else -> Unit
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    companion object {
        private const val PLAYBACK_DEF = "00:00"
    }
}
