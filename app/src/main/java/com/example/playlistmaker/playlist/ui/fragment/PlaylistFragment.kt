package com.example.playlistmaker.playlist.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.media.domain.models.Playlist
import com.example.playlistmaker.playlist.ui.view_model.PlaylistState
import com.example.playlistmaker.playlist.ui.view_model.PlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {

    private val viewModel: PlaylistViewModel by viewModel()
    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!

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
            is PlaylistState.Content -> showContent(state.playlist, state.duration)
        }
    }

    private fun showContent(playlist: Playlist, duration: Int) {
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
    }

    companion object {
        private const val ARGS_PLAYLIST_ID = "playlist"
        fun createArgs(playlistId: Long) = Bundle().apply { putLong(ARGS_PLAYLIST_ID, playlistId) }
    }
}
