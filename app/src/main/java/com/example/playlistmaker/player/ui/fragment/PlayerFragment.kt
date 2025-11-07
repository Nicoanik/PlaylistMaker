package com.example.playlistmaker.player.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.player.ui.view_model.PlayerState
import com.example.playlistmaker.player.ui.view_model.PlayerViewModel
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.models.dpToPx
import com.example.playlistmaker.search.domain.models.timeConversion
import com.google.gson.Gson
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.getValue

class PlayerFragment : Fragment() {

    companion object {
        private const val ARGS_TRACK = "track"

        fun createArgs(trackGson: String): Bundle =
            bundleOf(ARGS_TRACK to trackGson)
    }

    private val gson: Gson by inject()

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val mediaTrack = gson.fromJson(intent.getStringExtra(SearchFragment.Companion.TRACK_INTENT), Track::class.java)
        val mediaTrack = gson.fromJson(requireArguments().getString(ARGS_TRACK), Track::class.java)
        val viewModel by viewModel<PlayerViewModel> { parametersOf(mediaTrack.previewUrl) }

        viewModel.observePlayerState().observe(viewLifecycleOwner) {
            render(it)
        }

        Glide.with(this)
            .load(mediaTrack.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.album_cover_placeholder)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(8, requireContext())))
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
            findNavController().navigateUp()
        }

        binding.playButton.setOnClickListener {
            viewModel.onPlayButtonClicked()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
