package com.example.playlistmaker.playlist.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.media.domain.models.Playlist
import com.example.playlistmaker.media.domain.models.dpToPx
import com.example.playlistmaker.media.ui.fragment.CreatePlaylistFragment
import com.example.playlistmaker.playlist.ui.view_model.EditPlaylistState
import com.example.playlistmaker.playlist.ui.view_model.EditPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class EditPlaylistFragment : CreatePlaylistFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val playlistId = arguments?.getLong(ARGS_PLAYLIST_ID)

        val viewModel: EditPlaylistViewModel by viewModel { parametersOf(playlistId) }

        binding.heading.text = resources.getString(R.string.edit)
        binding.buttonCreate.text = resources.getString(R.string.save)

        viewModel.state.observe(viewLifecycleOwner) {
            renderState(it)
        }

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.buttonCreate.setOnClickListener {
            viewModel.updatePlaylist(
                binding.etTitle.text.toString().trim(),
                binding.etDescription.text.toString().trim(),
                coverUri
                )
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                }
            })
    }

    private fun renderState(state: EditPlaylistState) {
        when (state) {
            is EditPlaylistState.Content -> showContent(state.playlist)
            is EditPlaylistState.Done -> findNavController().navigateUp()
        }
    }

    private fun showContent(playlist: Playlist) {
        Glide.with(requireContext())
            .load(playlist.coverPath)
            .placeholder(R.drawable.album_cover_placeholder)
            .transform(
                CenterCrop(),
                RoundedCorners(dpToPx(8, requireContext()))
            )
            .into(binding.ivPlaylistCover)
        binding.etTitle.setText(playlist.title)
        binding.etDescription.setText(playlist.description)
    }

    companion object {
        private const val ARGS_PLAYLIST_ID = "playlist"
        fun createArgs(playlistId: Long) = Bundle().apply { putLong(ARGS_PLAYLIST_ID, playlistId) }
    }
}
