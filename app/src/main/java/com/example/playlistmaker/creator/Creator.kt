package com.example.playlistmaker.creator

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.search.data.impl.SearchHistoryRepositoryImpl
import com.example.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.example.playlistmaker.search.data.impl.SearchTracksRepositoryImpl
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.domain.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.SearchHistoryRepository
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.SettingsRepository
import com.example.playlistmaker.search.domain.SearchTracksInteractor
import com.example.playlistmaker.search.domain.SearchTracksRepository
import com.example.playlistmaker.search.domain.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.example.playlistmaker.search.domain.impl.SearchTracksInteractorImpl
import com.example.playlistmaker.App

object Creator {

    private lateinit var application: Application

    fun initApplication (application: Application) {
        this.application = application
    }

    private fun provideSharedPreferences() : SharedPreferences {
        return  application.getSharedPreferences(App.Companion.PLAYLIST_MAKER_PREFERENCES, Context.MODE_PRIVATE)
    }

    private fun getTracksRepository(): SearchTracksRepository {
        return SearchTracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideSearchTracksInteractor(): SearchTracksInteractor {
        return SearchTracksInteractorImpl(getTracksRepository())
    }

    private fun getSearchHistoryRepository(): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(provideSharedPreferences())
    }

    fun provideSearchHistoryInteractor(): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(getSearchHistoryRepository())
    }

    private fun getSettingsRepository(): SettingsRepository {
        return SettingsRepositoryImpl(provideSharedPreferences())
    }

    fun provideSettingsInteractor(): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository())
    }
}