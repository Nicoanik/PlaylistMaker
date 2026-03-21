package com.example.playlistmaker.di

import com.example.playlistmaker.data.media.impl.FavoritesRepositoryImpl
import com.example.playlistmaker.data.media.impl.PlaylistRepositoryImpl
import com.example.playlistmaker.domain.media.FavoritesRepository
import com.example.playlistmaker.domain.media.PlaylistRepository
import com.example.playlistmaker.data.search.impl.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.search.impl.SearchTracksRepositoryImpl
import com.example.playlistmaker.domain.search.SearchHistoryRepository
import com.example.playlistmaker.domain.search.SearchTracksRepository
import com.example.playlistmaker.data.settings.impl.SettingsRepositoryImpl
import com.example.playlistmaker.domain.settings.SettingsRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {

    factory<SearchTracksRepository> {
        SearchTracksRepositoryImpl(get())
    }

    factory<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get(), get())
    }

    factory<SettingsRepository> {
        SettingsRepositoryImpl(get(), androidContext())
    }

    factory<FavoritesRepository> {
        FavoritesRepositoryImpl(get(), get())
    }

    factory<PlaylistRepository> {
        PlaylistRepositoryImpl(
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
}
