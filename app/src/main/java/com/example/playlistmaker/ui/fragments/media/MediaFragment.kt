package com.example.playlistmaker.ui.fragments.media

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.fragments.player.PlayerFragment
import com.example.playlistmaker.ui.fragments.playlist.PlaylistFragment
import com.example.playlistmaker.ui.screens.media.MediaScreen
import com.example.playlistmaker.ui.theme.PlaylistMakerTheme
import com.example.playlistmaker.presentation.media.MediaViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaFragment : Fragment() {

    private val viewModel by viewModel<MediaViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        // Устанавливаем стратегию уничтожения композиции
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {
            PlaylistMakerTheme {
                MediaScreen(
                    viewModel = viewModel,
                    onTrackClick = { track ->
                        findNavController().navigate(
                            R.id.action_mediaFragment_to_playerFragment,
                            PlayerFragment.createArgs(track)
                        )
                    },
                    onNewPlaylistClick = {
                        findNavController().navigate(
                            R.id.action_mediaFragment_to_newPlaylist
                        )
                    },
                    onPlaylistClick = { playlist ->
                        findNavController().navigate(
                            R.id.action_mediaFragment_to_playlistFragment,
                            PlaylistFragment.createArgs(playlist.id)
                        )
                    }
                )
            }
        }
    }

}