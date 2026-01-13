package com.example.playlistmaker.di

import com.example.playlistmaker.media.data.impl.FavoritesRepositoryImpl
import com.example.playlistmaker.media.data.impl.PlaylistRepositoryImpl
import com.example.playlistmaker.media.domain.FavoritesRepository
import com.example.playlistmaker.media.domain.PlaylistRepository
import com.example.playlistmaker.search.data.impl.SearchHistoryRepositoryImpl
import com.example.playlistmaker.search.data.impl.SearchTracksRepositoryImpl
import com.example.playlistmaker.search.domain.SearchHistoryRepository
import com.example.playlistmaker.search.domain.SearchTracksRepository
import com.example.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.SettingsRepository
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
