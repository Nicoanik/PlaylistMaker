package com.example.playlistmaker.media.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.media.domain.models.Playlist
import com.example.playlistmaker.media.ui.view_model.PlaylistsState
import com.example.playlistmaker.media.ui.view_model.PlaylistsViewModel
import com.example.playlistmaker.playlist.ui.fragment.PlaylistFragment
import com.example.playlistmaker.utils.clickDebounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    private val viewModel: PlaylistsViewModel by viewModel()

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!

    private lateinit var onPlaylistClickDebounce: (Playlist) -> Unit

    private lateinit var adapter: PlaylistAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getPlaylists()

        onPlaylistClickDebounce = clickDebounce(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope
        ) { playlist ->
            findNavController().navigate(
                R.id.action_mediaFragment_to_playlistFragment,
                PlaylistFragment.createArgs(playlist.id)
            )
        }

        adapter = PlaylistAdapter { playlist -> onPlaylistClickDebounce(playlist) }
        binding.rvPlaylists.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvPlaylists.adapter = adapter

        viewModel.state.observe(viewLifecycleOwner) {
            renderSearch(it)
        }

        binding.newPlaylistButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_mediaFragment_to_newPlaylist
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun renderSearch(state: PlaylistsState) {
        when (state) {
            is PlaylistsState.Empty -> showPlaceholder()
            is PlaylistsState.Content -> showContent(state.playlists)
        }
    }

    private fun showPlaceholder() {
        binding.apply {
            rvPlaylists.isVisible = false
            ivPlaceholderImage.isVisible = true
            tvPlaceholderMessage.isVisible = true
        }
    }

    private fun showContent(playlists: List<Playlist>) {
        adapter.playlists.clear()
        adapter.playlists.addAll(playlists)
        adapter.notifyDataSetChanged()
        binding.apply {
            ivPlaceholderImage.isVisible = false
            tvPlaceholderMessage.isVisible = false
            rvPlaylists.isVisible = true
        }
    }

    companion object {
        fun newInstance() = PlaylistsFragment()

        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}
