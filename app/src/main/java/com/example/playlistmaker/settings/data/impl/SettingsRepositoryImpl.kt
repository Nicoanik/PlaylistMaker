package com.example.playlistmaker.settings.data.impl

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import com.example.playlistmaker.settings.domain.SettingsRepository

class SettingsRepositoryImpl(
    private val sharedPrefs: SharedPreferences,
    private val context: Context
    ) : SettingsRepository {

    override fun saveSettingsThemeMode(set: Boolean) {
        sharedPrefs.edit { putBoolean(THEME_MODE_KEY, set) }
    }

    override fun getSettingThemMode(): Boolean {
        if (sharedPrefs.contains(THEME_MODE_KEY)) {
            val check = sharedPrefs.getBoolean(THEME_MODE_KEY, false)
            setTheme(check)
            return check
        } else {
            setTheme(checkThemeMode())
            return checkThemeMode()
        }
    }

    fun setTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    fun checkThemeMode(): Boolean {
        return when (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            else -> false
        }
    }

    companion object {
        const val THEME_MODE_KEY = "theme_mode_key"
    }
}
