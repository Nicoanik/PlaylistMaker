package com.example.playlistmaker.search.ui.fragment

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.player.ui.fragment.PlayerFragment
import com.example.playlistmaker.search.ui.screen.SearchScreen
import com.example.playlistmaker.search.ui.view_model.SearchViewModel
import com.example.playlistmaker.settings.ui.theme.PlaylistMakerTheme
import com.example.playlistmaker.utils.ConnectedBroadcastReceiver
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class SearchFragment : Fragment() {

    private val viewModel by viewModel<SearchViewModel>()

    private val receiver by lazy { ConnectedBroadcastReceiver() }
    private val filter = IntentFilter().apply {
        addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        addAction("android.net.conn.CONNECTIVITY_CHANGE")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                PlaylistMakerTheme {
                    SearchScreen(
                        viewModel = viewModel,
                        onNavigateToPlayer = { track ->
                            findNavController().navigate(
                                R.id.action_searchFragment_to_playerFragment,
                                PlayerFragment.createArgs(track)
                            )
                        }
                    )
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onResume() {
        super.onResume()
        requireActivity().registerReceiver(receiver, filter, Context.RECEIVER_NOT_EXPORTED)
    }

    override fun onPause() {
        super.onPause()
        requireActivity().unregisterReceiver(receiver)
    }

}