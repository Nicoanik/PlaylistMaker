package com.example.playlistmaker

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit

class App: Application() {

    var darkTheme = false
    var themeModeKeyActive = false

    override fun onCreate() {
        super.onCreate()

        val sharedPrefs = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)

//        sharedPrefs.edit { remove(THEME_MODE_KEY) } // Для проверки холодного старта

        if (sharedPrefs.contains(THEME_MODE_KEY)) {
            darkTheme = sharedPrefs.getBoolean(THEME_MODE_KEY, false)
            switchTheme(darkTheme)
        } else {
            checkThemeMode()
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
        darkTheme = when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            Configuration.UI_MODE_NIGHT_NO -> false
            Configuration.UI_MODE_NIGHT_UNDEFINED -> false
            else -> false
        }
        return darkTheme
    }

    companion object {
        const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"
        const val THEME_MODE_KEY = "theme_mode_key"
    }
}