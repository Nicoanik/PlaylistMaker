package com.example.playlistmaker.presentation

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.Creator

class App: Application() {

    companion object {
        const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"
    }

    var darkTheme = false
    var themeModeKeyActive = false

    override fun onCreate() {
        super.onCreate()

        Creator.initApplication(this)
        val settingsAppInteractor = Creator.provideSettingsAppInteractor()

        if (settingsAppInteractor.checkSettingsThemeMode()) {
            switchTheme(settingsAppInteractor.getSettingThemMode())
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
}
