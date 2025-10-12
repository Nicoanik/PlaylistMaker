package com.example.playlistmaker.di

import android.content.Context
import com.example.playlistmaker.App.Companion.ITUNES_BASE_URL
import com.example.playlistmaker.App.Companion.PLAYLIST_MAKER_PREFERENCES
import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.network.ItunesApiService
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single<NetworkClient> {
        RetrofitNetworkClient(androidContext(), get())
    }

    single<ItunesApiService> {
        Retrofit.Builder()
            .baseUrl(ITUNES_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ItunesApiService::class.java)
    }

    single {
        androidContext().getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, Context.MODE_PRIVATE)
    }

    factory {
        Gson()
    }
}
