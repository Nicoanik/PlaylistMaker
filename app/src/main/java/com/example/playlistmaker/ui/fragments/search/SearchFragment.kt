package com.example.playlistmaker.ui.fragments.search

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.fragments.player.PlayerFragment
import com.example.playlistmaker.ui.screens.search.SearchScreen
import com.example.playlistmaker.presentation.search.SearchViewModel
import com.example.playlistmaker.ui.theme.PlaylistMakerTheme
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class SearchFragment : Fragment() {

    private val viewModel by viewModel<SearchViewModel>()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        // Устанавливаем стратегию уничтожения композиции
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
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