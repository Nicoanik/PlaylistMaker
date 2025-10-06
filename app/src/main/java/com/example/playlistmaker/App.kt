package com.example.playlistmaker

import android.app.Application
import com.example.playlistmaker.creator.Creator

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        Creator.initApplication(this)
        val settingsInteractor = Creator.provideSettingsInteractor()
        settingsInteractor.getSettingThemMode()
    }

    companion object {
        const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"
    }
}
