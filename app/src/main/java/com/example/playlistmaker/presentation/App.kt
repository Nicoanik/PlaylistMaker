package com.example.playlistmaker.presentation

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.Creator

class App: Application() {

    var darkTheme = false
    var themeModeKeyActive = false

    override fun onCreate() {
        super.onCreate()

        Creator.initApplication(this)

        val sharedPrefs = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)

        if (sharedPrefs.contains(THEME_MODE_KEY)) {
            switchTheme(sharedPrefs.getBoolean(THEME_MODE_KEY, false))
        } else {
            darkTheme = checkThemeMode()
        }
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        themeModeKeyActive = true
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    fun checkThemeMode(): Boolean {
        return when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            else -> false
        }
    }

    companion object {
        const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"
        const val THEME_MODE_KEY = "theme_mode_key"
    }
}