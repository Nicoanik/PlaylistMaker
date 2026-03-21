package com.example.playlistmaker.ui.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.example.playlistmaker.ui.screens.settings.SettingsScreen
import com.example.playlistmaker.ui.theme.PlaylistMakerTheme
import com.example.playlistmaker.presentation.settings.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        // Устанавливаем стратегию уничтожения композиции
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {
            PlaylistMakerTheme() {
                SettingsScreen(
                    viewModel = viewModel
                )
            }
        }
    }

}