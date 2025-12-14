package com.example.playlistmaker.player.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
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
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.getValue

class PlayerFragment : Fragment() {

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

        val track: Track = arguments?.getParcelable(ARGS_TRACK)!!

        val viewModel by viewModel<PlayerViewModel> { parametersOf(track) }

        viewModel.playerState().observe(viewLifecycleOwner) {
            render(it)
        }

        viewModel.favouriteState().observe(viewLifecycleOwner) {
            favoriteIcon(it)
        }

        Glide.with(this)
            .load(track.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.album_cover_placeholder)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(8, requireContext())))
            .into(binding.ivAlbumCover)
        binding.apply {
            tvTrackName.text = track.trackName
            tvArtistName.text = track.artistName
            tvTrackTime.text = timeConversion(track.trackTime)
            tvCollectionName.text = track.collectionName
            tvReleaseDate.text = track.releaseDate?.substring(0, 4)
            tvPrimaryGenreName.text = track.primaryGenreName
            tvCountry.text = track.country
        }

        binding.backButtonAudioPlayer.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.playButton.setOnClickListener {
            viewModel.onPlayButtonClicked()
        }

        binding.favoriteButton.setOnClickListener {
            viewModel.onFavoriteButton()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(state: PlayerState) {
        when (state) {
            is PlayerState.Default -> binding.tvPlaybackProgress.text = state.progress
            is PlayerState.Prepared -> {
                binding.playButton.isVisible = true
                binding.tvPlaybackProgress.text = state.progress
            }
            is PlayerState.Playing -> {
                binding.playButton.setImageResource(R.drawable.pause_button_100)
                binding.tvPlaybackProgress.text = state.progress
            }
            is PlayerState.Paused -> {
                binding.playButton.setImageResource(R.drawable.play_button_100)
                binding.tvPlaybackProgress.text = state.progress
            }
        }
    }

    private fun favoriteIcon(favorite: Boolean) {
        if (favorite) {
            binding.favoriteButton.setImageResource(R.drawable.button_favorite_true_51)
        } else {
            binding.favoriteButton.setImageResource(R.drawable.button_favorite_false_51)
        }
    }

    companion object {
        private const val ARGS_TRACK = "track"
        fun createArgs(track: Track) = Bundle().apply { putParcelable(ARGS_TRACK, track) }
    }
}
