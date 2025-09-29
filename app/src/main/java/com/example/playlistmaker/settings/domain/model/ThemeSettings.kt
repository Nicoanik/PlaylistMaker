package com.example.playlistmaker.settings.domain.model

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.creator.Creator

class ThemeSettings: Application() {

    var darkTheme = false
    var themeModeKeyActive = false

    override fun onCreate() {
        super.onCreate()

        Creator.initApplication(this)
        val settingsInteractor = Creator.provideSettingsInteractor()

        if (settingsInteractor.checkSettingsThemeMode()) {
            switchTheme(settingsInteractor.getSettingThemMode())
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
    }
}