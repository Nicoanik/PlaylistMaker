package com.example.playlistmaker.player.ui.activity

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
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.models.dpToPx
import com.example.playlistmaker.search.domain.models.timeConversion
import com.example.playlistmaker.search.ui.activity.SearchActivity
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding

    enum class PlayerState(val state: Int) {
        DEFAULT(0),
        PREPARED(1),
        PLAYING(2),
        PAUSED(3)
    }

    private var mediaPlayer = MediaPlayer()
    private var playerState = PlayerState.DEFAULT

    private val mainHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_player)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val mediaTrack = Gson().fromJson(intent.getStringExtra(SearchActivity.Companion.TRACK_INTENT), Track::class.java)

        Glide.with(this)
            .load(mediaTrack.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.album_cover_placeholder)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(8, this)))
            .into(binding.ivAlbumCover)
        binding.apply {
            tvTrackName.text = mediaTrack.trackName
            tvArtistName.text = mediaTrack.artistName
            tvTrackTime.text = timeConversion(mediaTrack.trackTime)
            tvCollectionName.text = mediaTrack.collectionName
            tvReleaseDate.text = mediaTrack.releaseDate.substring(0, 4)
            tvPrimaryGenreName.text = mediaTrack.primaryGenreName
            tvCountry.text = mediaTrack.country
            playbackProgress.text = PLAYBACK_DEF
        }

        binding.backButtonAudioPlayer.setOnClickListener {
            finish()
        }

        preparePlayer(mediaTrack)

        binding.playButton.setOnClickListener {
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
            binding.playButton.setImageResource(R.drawable.play_button_100)
            playerState = PlayerState.PREPARED
            mainHandler.removeCallbacksAndMessages(null)
            binding.playbackProgress.text = PLAYBACK_DEF
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        binding.playButton.setImageResource(R.drawable.pause_button_100)
        playerState = PlayerState.PLAYING
        mainHandler.post(
            object : Runnable {
                override fun run() {
                    binding.playbackProgress.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
                    mainHandler.postDelayed(this, 500)
                }
            }
        )
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        binding.playButton.setImageResource(R.drawable.play_button_100)
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
