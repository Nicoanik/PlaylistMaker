package com.example.playlistmaker.di

import android.content.Context
import androidx.room.Room
import com.example.playlistmaker.BuildConfig.ITUNES_BASE_URL
import com.example.playlistmaker.BuildConfig.PLAYLIST_MAKER_PREFERENCES
import com.example.playlistmaker.data.media.converters.PlaylistDbConverter
import com.example.playlistmaker.data.media.converters.PlaylistTrackDbConvertor
import com.example.playlistmaker.data.media.converters.TrackDbConverter
import com.example.playlistmaker.data.media.db.AppDatabase
import com.example.playlistmaker.data.media.db.dao.FavoriteTrackDao
import com.example.playlistmaker.data.media.db.dao.PlaylistDao
import com.example.playlistmaker.data.media.db.dao.PlaylistTrackDao
import com.example.playlistmaker.data.search.NetworkClient
import com.example.playlistmaker.data.search.network.ItunesApiService
import com.example.playlistmaker.data.search.network.RetrofitNetworkClient
import com.example.playlistmaker.services.ExternalNavigator
import com.google.firebase.analytics.FirebaseAnalytics
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

    factory { Gson() }

    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "database.db"
        )
            .build()
    }

    single<ExternalNavigator> { ExternalNavigator(get()) }

    factory { PlaylistDbConverter(get()) }

    factory { PlaylistTrackDbConvertor() }

    factory { TrackDbConverter() }

    factory<FavoriteTrackDao> { get<AppDatabase>().favoriteTrackDao() }

    factory<PlaylistDao> { get<AppDatabase>().playlistDao() }

    factory<PlaylistTrackDao> { get<AppDatabase>().playlistTrackDao() }

    factory { FirebaseAnalytics.getInstance(androidContext()) }
}
