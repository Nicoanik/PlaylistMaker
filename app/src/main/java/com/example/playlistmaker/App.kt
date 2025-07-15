package com.example.playlistmaker

import android.app.Application
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit

class App: Application() {

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()

        val sharedPrefs = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)

        if (sharedPrefs.contains(THEME_MODE)) {
            darkTheme = sharedPrefs.getBoolean(THEME_MODE, false)
            switchTheme(darkTheme)
        } else {
            darkTheme = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
        }
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    companion object {
        const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"
        const val THEME_MODE = "theme_mode"
    }
}