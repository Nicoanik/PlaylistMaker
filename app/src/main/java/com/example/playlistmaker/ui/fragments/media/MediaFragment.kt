package com.example.playlistmaker.ui.fragments.media

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.example.playlistmaker.ui.screens.media.MediaScreen
import com.example.playlistmaker.ui.theme.PlaylistMakerTheme
import com.google.android.material.tabs.TabLayoutMediator

class MediaFragment : Fragment() {

    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                PlaylistMakerTheme {
                    MediaScreen()
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
