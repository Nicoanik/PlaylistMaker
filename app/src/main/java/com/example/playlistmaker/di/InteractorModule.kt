package com.example.playlistmaker.di

import com.example.playlistmaker.search.domain.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.SearchTracksInteractor
import com.example.playlistmaker.search.domain.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.search.domain.impl.SearchTracksInteractorImpl
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.example.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val interactorModule = module {

    factory <SearchTracksInteractor> {
        SearchTracksInteractorImpl(get())
    }

    factory <SearchHistoryInteractor> {
        SearchHistoryInteractorImpl(get())
    }

    factory <SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    factory <SharingInteractor> {
        SharingInteractorImpl(get(),androidContext())
    }
}
