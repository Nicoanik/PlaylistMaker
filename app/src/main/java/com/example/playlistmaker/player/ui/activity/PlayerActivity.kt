package com.example.playlistmaker.player.ui.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.player.ui.view_model.PlayerState
import com.example.playlistmaker.player.ui.view_model.PlayerViewModel
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.models.dpToPx
import com.example.playlistmaker.search.domain.models.timeConversion
import com.example.playlistmaker.search.ui.activity.SearchActivity
import com.google.gson.Gson
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.getValue

class PlayerActivity : AppCompatActivity() {

    private val gson: Gson by inject()

    private lateinit var binding: ActivityPlayerBinding

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

        val mediaTrack = gson.fromJson(intent.getStringExtra(SearchActivity.Companion.TRACK_INTENT), Track::class.java)
        val viewModel by viewModel<PlayerViewModel> { parametersOf(mediaTrack.previewUrl) }

        viewModel.observePlayerState().observe(this) {
            render(it)
        }

        Glide.with(this)
            .load(mediaTrack.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.album_cover_placeholder)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(8, this)))
            .into(binding.ivAlbumCover)
        binding.apply {
            tvTrackName.text = mediaTrack.trackName
            tvArtistName.text = mediaTrack.artistName
            tvTrackTime.text = timeConversion(mediaTrack.trackTime)
            tvCollectionName.text = mediaTrack.collectionName
            tvReleaseDate.text = mediaTrack.releaseDate?.substring(0, 4)
            tvPrimaryGenreName.text = mediaTrack.primaryGenreName
            tvCountry.text = mediaTrack.country
        }

        binding.backButtonAudioPlayer.setOnClickListener {
            finish()
        }

        binding.playButton.setOnClickListener {
            viewModel.onPlayButtonClicked()
        }
    }
    private fun changeButtonImage(isPlaying: Boolean) {
        if (isPlaying) {
            binding.playButton.setImageResource(R.drawable.pause_button_100)
        } else {
            binding.playButton.setImageResource(R.drawable.play_button_100)
        }
    }

    private fun render(state: PlayerState) {
        changeButtonImage(state is PlayerState.Playing)
        when (state) {
            is PlayerState.Prepared -> binding.tvPlaybackProgress.text = state.timer
            is PlayerState.Playing -> binding.tvPlaybackProgress.text = state.timer
            is PlayerState.Paused -> binding.tvPlaybackProgress.text = state.timer
            is PlayerState.Completion -> binding.tvPlaybackProgress.text = state.timer
        }
    }
}
