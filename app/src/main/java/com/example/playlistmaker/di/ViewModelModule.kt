package com.example.playlistmaker.di

import com.example.playlistmaker.presentation.media.CreatePlaylistViewModel
import com.example.playlistmaker.presentation.player.PlayerViewModel
import com.example.playlistmaker.domain.media.models.Track
import com.example.playlistmaker.presentation.media.MediaViewModel
import com.example.playlistmaker.presentation.playlist.EditPlaylistViewModel
import com.example.playlistmaker.presentation.playlist.PlaylistViewModel
import com.example.playlistmaker.presentation.search.SearchViewModel
import com.example.playlistmaker.presentation.settings.SettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { SearchViewModel(get(), get()) }

    viewModel { SettingsViewModel(get(), get()) }

    viewModel { (track: Track) ->
        PlayerViewModel(track, get(), get(), get())
    }

    viewModel { CreatePlaylistViewModel(get()) }

    viewModel { (playlistId: Long) -> PlaylistViewModel(playlistId, get()) }

    viewModel { (playlistId: Long) -> EditPlaylistViewModel(playlistId, get()) }

    viewModel { MediaViewModel(get(), get()) }
}
