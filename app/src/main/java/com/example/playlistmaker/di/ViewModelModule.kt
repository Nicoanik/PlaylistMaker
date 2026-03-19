package com.example.playlistmaker.di

import com.example.playlistmaker.ui.view_models.media.CreatePlaylistViewModel
import com.example.playlistmaker.ui.view_models.media.FavoriteTracksViewModel
import com.example.playlistmaker.ui.view_models.media.PlaylistsViewModel
import com.example.playlistmaker.ui.view_models.player.PlayerViewModel
import com.example.playlistmaker.domain.media.models.Track
import com.example.playlistmaker.ui.view_models.media.MediaViewModel
import com.example.playlistmaker.ui.view_models.playlist.EditPlaylistViewModel
import com.example.playlistmaker.ui.view_models.playlist.PlaylistViewModel
import com.example.playlistmaker.ui.view_models.search.SearchViewModel
import com.example.playlistmaker.ui.view_models.settings.SettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { SearchViewModel(get(), get()) }

    viewModel { SettingsViewModel(get(), get()) }

    viewModel { (track: Track) ->
        PlayerViewModel(track, get(), get(), get())
    }

    viewModel { FavoriteTracksViewModel(get()) }

    viewModel { PlaylistsViewModel(get()) }

    viewModel { CreatePlaylistViewModel(get()) }

    viewModel { (playlistId: Long) -> PlaylistViewModel(playlistId, get()) }

    viewModel { (playlistId: Long) -> EditPlaylistViewModel(playlistId, get()) }

    viewModel { MediaViewModel(get(), get()) }
}
