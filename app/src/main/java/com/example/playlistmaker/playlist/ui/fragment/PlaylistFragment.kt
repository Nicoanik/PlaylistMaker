package com.example.playlistmaker.playlist.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.media.domain.models.Playlist
import com.example.playlistmaker.media.domain.models.Track
import com.example.playlistmaker.player.ui.fragment.PlayerFragment
import com.example.playlistmaker.playlist.ui.view_model.PlaylistState
import com.example.playlistmaker.playlist.ui.view_model.PlaylistViewModel
import com.example.playlistmaker.utils.debounce
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {

    private val viewModel: PlaylistViewModel by viewModel()
    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: PlaylistAdapter

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

        viewModel.state.observe(viewLifecycleOwner) {
            renderState(it)
        }

        val playlistId = arguments?.getLong(ARGS_PLAYLIST_ID)!!

        viewModel.getPlaylistById(playlistId)

        onTrackClickDebounce = debounce(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { track ->
            findNavController().navigate(
                R.id.action_playlistFragment_to_playerFragment,
                PlayerFragment.createArgs(track)
            )
        }

        adapter = PlaylistAdapter(
            { track -> onTrackClickDebounce(track) },
            { track -> showDialog(track.trackId) }
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun renderState(state: PlaylistState) {
        when (state) {
            is PlaylistState -> showContent(state.playlist, state.duration, state.tracks)
        }
    }

    private fun showContent(playlist: Playlist, duration: Int, tracks: List<Track>) {
        Glide.with(this)
            .load(playlist.coverUri)
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
        adapter.tracks.clear()
        adapter.tracks.addAll(tracks)
        adapter.notifyDataSetChanged()
    }

    private fun showDialog(trackId: Long?) {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(R.string.delete_track_dialog)
            .setNegativeButton(R.string.yes) { _, _ ->
                viewModel.deleteTrackById(trackId)
            }
            .setNeutralButton(R.string.no) { _, _ -> }
            .show()
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val ARGS_PLAYLIST_ID = "playlist"
        fun createArgs(playlistId: Long) = Bundle().apply { putLong(ARGS_PLAYLIST_ID, playlistId) }
    }
}
