package com.example.playlistmaker.playlist.ui.fragment

import android.os.Bundle
import com.example.playlistmaker.media.ui.fragment.CreatePlaylistFragment
import com.example.playlistmaker.playlist.ui.view_model.EditPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditPlaylistFragment: CreatePlaylistFragment() {

    override val viewModel: EditPlaylistViewModel by viewModel()

    val playlistId = arguments?.getLong(ARGS_PLAYLIST_ID)

    companion object {
        private const val ARGS_PLAYLIST_ID = "playlist"
        fun createArgs(playlistId: Long) = Bundle().apply { putLong(ARGS_PLAYLIST_ID, playlistId) }
    }
}
