package com.example.playlistmaker.ui.fragments.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.domain.media.models.Playlist
import com.example.playlistmaker.domain.media.models.Track
import com.example.playlistmaker.domain.media.models.dpToPx
import com.example.playlistmaker.ui.fragments.player.PlayerFragment
import com.example.playlistmaker.presentation.playlist.PlaylistState
import com.example.playlistmaker.presentation.playlist.PlaylistViewModel
import com.example.playlistmaker.utils.antiRepetition
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf

class PlaylistFragment : Fragment() {

    private lateinit var viewModel: PlaylistViewModel
    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: PlaylistAdapter

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    private lateinit var onTrackClickDebounce: (Track) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val playlistId = arguments?.getLong(ARGS_PLAYLIST_ID)

        viewModel = getViewModel { parametersOf(playlistId) }

        viewModel.loadContent()

        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetMore).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
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

        viewModel.state.observe(viewLifecycleOwner) {
            renderState(it)
        }

        onTrackClickDebounce = antiRepetition(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope
        ) { track ->
            findNavController().navigate(
                R.id.action_playlistFragment_to_playerFragment,
                PlayerFragment.createArgs(track)
            )
        }

        adapter = PlaylistAdapter(
            { track -> onTrackClickDebounce(track) },
            { track -> deleteTrackDialog(track.trackId) }
        )
        binding.rvTracks.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.rvTracks.adapter = adapter

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.sharePlaylist.setOnClickListener {
            viewModel.sharePlaylist()
        }

        binding.menu.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.overlay.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.sharePlaylistBottomSheet.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            viewModel.sharePlaylist()
        }

        binding.editBottomSheet.setOnClickListener {
            findNavController().navigate(
                R.id.action_playlistFragment_to_editPlaylistFragment,
                EditPlaylistFragment.createArgs(playlistId ?: return@setOnClickListener)
            )
        }

        binding.deletePlaylistBottomSheet.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            deletePlaylistDialog()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun renderState(state: PlaylistState) {
        when (state) {
            is PlaylistState.Content -> showContent(state.playlist, state.duration, state.tracks)
            is PlaylistState.Share -> {
                if (!state.isSharing) Toast.makeText(
                    requireContext(),
                    R.string.nothing_share,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun showContent(playlist: Playlist, duration: Int, tracks: List<Track>) {
        Glide.with(this)
            .load(playlist.coverPath)
            .placeholder(R.drawable.album_cover_placeholder)
            .error(R.drawable.album_cover_placeholder)
            .centerCrop()
            .into(binding.ivCover)
        binding.apply {
            tvTitle.text = playlist.title
            tvDescription.text = playlist.description
            tvDuration.text = resources.getQuantityString(
                R.plurals.tracks_duration,
                duration,
                duration
            )
            tvPlaylistSize.text = resources.getQuantityString(
                R.plurals.playlist_size,
                playlist.playlistSize,
                playlist.playlistSize
            )
        }

        binding.tvNoTracks.isVisible = tracks.isEmpty()
        adapter.tracks.clear()
        adapter.tracks.addAll(tracks)
        adapter.notifyDataSetChanged()

        Glide.with(this)
            .load(playlist.coverPath)
            .placeholder(R.drawable.album_cover_placeholder)
            .error(R.drawable.album_cover_placeholder)
            .transform(
                CenterCrop(),
                RoundedCorners(dpToPx(2, requireContext()))
            )
            .into(binding.ivPlaylistCoverBottomSheet)
        binding.tvPlaylistTitleBottomSheet.text = playlist.title
        binding.tvPlaylistSizeBottomSheet.text = resources.getQuantityString(
            R.plurals.playlist_size,
            playlist.playlistSize,
            playlist.playlistSize
        )
    }

    private fun deleteTrackDialog(trackId: Long?) {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(R.string.delete_track_dialog)
            .setPositiveButton(R.string.yes) { _, _ ->
                viewModel.deleteTrackById(trackId ?: return@setPositiveButton)
            }
            .setNegativeButton(R.string.no) { _, _ -> }
            .show()
    }

    private fun deletePlaylistDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(getString(R.string.delete_playlist_dialog))
            .setPositiveButton(R.string.delete) { _, _ ->
                viewModel.deletePlaylist()
                findNavController().navigateUp()
            }
            .setNegativeButton(R.string.cancel) { _, _ -> }
            .show()
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val ARGS_PLAYLIST_ID = "playlist"
        fun createArgs(playlistId: Long) = Bundle().apply { putLong(ARGS_PLAYLIST_ID, playlistId) }
    }

}