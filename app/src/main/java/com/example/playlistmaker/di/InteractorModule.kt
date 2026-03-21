package com.example.playlistmaker.di

import com.example.playlistmaker.domain.media.FavoritesInteractor
import com.example.playlistmaker.domain.media.PlaylistInteractor
import com.example.playlistmaker.domain.media.impl.FavoritesInteractorImpl
import com.example.playlistmaker.domain.media.impl.PlaylistInteractorImpl
import com.example.playlistmaker.domain.search.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.SearchTracksInteractor
import com.example.playlistmaker.domain.search.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.search.impl.SearchTracksInteractorImpl
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.domain.sharing.impl.SharingInteractorImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val interactorModule = module {

    factory<SearchTracksInteractor> {
        SearchTracksInteractorImpl(get())
    }

    factory<SearchHistoryInteractor> {
        SearchHistoryInteractorImpl(get())
    }

    factory<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    factory<SharingInteractor> {
        SharingInteractorImpl(get(), androidContext())
    }

    factory<FavoritesInteractor> {
        FavoritesInteractorImpl(get())
    }

    factory<PlaylistInteractor> {
        PlaylistInteractorImpl(get(), get(), androidContext())
    }
}
