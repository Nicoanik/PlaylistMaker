package com.example.playlistmaker.player.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.media.domain.models.Playlist
import com.example.playlistmaker.player.ui.view_model.PlayerState
import com.example.playlistmaker.player.ui.view_model.PlayerViewModel
import com.example.playlistmaker.media.domain.models.Track
import com.example.playlistmaker.media.domain.models.dpToPx
import com.example.playlistmaker.media.domain.models.timeConversion
import com.example.playlistmaker.utils.debounce
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.getValue

class PlayerFragment : Fragment() {

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!

    private lateinit var onPlaylistClickDebounce: (Playlist) -> Unit

    private lateinit var adapter: PlayerAdapter

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

        onPlaylistClickDebounce = debounce(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { playlist ->
            viewModel.addTrackToPlaylist(playlist)
        }

        adapter = PlayerAdapter { playlist -> onPlaylistClickDebounce(playlist) }
        binding.rvAddToPlaylist.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.rvAddToPlaylist.adapter = adapter

        val bottomSheetContainer = binding.bottomSheet
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        viewModel.state().observe(viewLifecycleOwner) {
            render(it)
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

        binding.queueButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            viewModel.getPlaylists()
        }

        binding.addToPlaylistButton.setOnClickListener {
            findNavController().navigate(R.id.action_playerFragment_to_createPlaylist)
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(p0: View, p1: Int) {
                when (p1) {
                    BottomSheetBehavior.STATE_HIDDEN -> binding.overlay.isVisible = false
                    else -> binding.overlay.isVisible = true
                }
            }

            override fun onSlide(p0: View, p1: Float) {
                binding.overlay.alpha = (p1 + 1f) / 2f
            }
        })

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
            is PlayerState.Prepared -> {
                binding.playButton.setImageResource(R.drawable.play_button_100)
                binding.playButton.isVisible = state.isPlayButtonEnabled
                binding.tvPlaybackProgress.text = PLAYBACK_DEF
            }
            is PlayerState.Playing -> {
                binding.playButton.setImageResource(R.drawable.pause_button_100)
                binding.tvPlaybackProgress.text = state.progress
            }
            is PlayerState.Paused -> {
                binding.playButton.setImageResource(R.drawable.play_button_100)
                binding.tvPlaybackProgress.text = state.progress
            }
            is PlayerState.Favorite -> {
                when (state.isFavorite) {
                    true -> binding.favoriteButton.setImageResource(R.drawable.button_favorite_true_51)
                    false -> binding.favoriteButton.setImageResource(R.drawable.button_favorite_false_51)
                }
            }
            is PlayerState.InPlaylist -> {
                when (state.inPlaylist) {
                    false -> Toast.makeText(requireContext(), "Трек уже добавлен в плейлист [название плейлиста]", Toast.LENGTH_LONG).show()
                    true -> Toast.makeText(requireContext(), "Добавлено в плейлист [название плейлиста]", Toast.LENGTH_LONG).show()
                }
            }
            is PlayerState.BottomSheetContent -> {
                if (state.playlists.isNotEmpty()) {
                    adapter.playlists.clear()
                    adapter.playlists.addAll(state.playlists)
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }

    companion object {

        const val PLAYBACK_DEF = "00:00"
        private const val ARGS_TRACK = "track"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        fun createArgs(track: Track) = Bundle().apply { putParcelable(ARGS_TRACK, track) }
    }
}
