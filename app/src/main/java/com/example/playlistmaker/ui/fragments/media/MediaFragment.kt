package com.example.playlistmaker.ui.fragments.media

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.fragments.player.PlayerFragment
import com.example.playlistmaker.ui.screens.media.MediaScreen
import com.example.playlistmaker.ui.theme.PlaylistMakerTheme
import com.example.playlistmaker.ui.view_models.media.MediaViewModel
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaFragment : Fragment() {

    private val viewModel by viewModel<MediaViewModel>()

//    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                PlaylistMakerTheme {
                    MediaScreen(
                        viewModel = viewModel,
                        onTrackClick = { track ->
                            findNavController().navigate(
                                R.id.action_mediaFragment_to_playerFragment,
                                PlayerFragment.createArgs(track)
                            )
                        }
                    )
                }
            }
        }
    }
}

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        binding.viewPager.adapter = MediaViewPagerAdapter(
//            fragmentManager = childFragmentManager,
//            lifecycle = lifecycle
//        )
//
//        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
//            when (position) {
//                0 -> tab.text = getString(R.string.favorite_tracks)
//                1 -> tab.text = getString(R.string.playlists)
//            }
//        }
//        tabMediator.attach()
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        tabMediator.detach()
//        _binding = null
//    }
//}
