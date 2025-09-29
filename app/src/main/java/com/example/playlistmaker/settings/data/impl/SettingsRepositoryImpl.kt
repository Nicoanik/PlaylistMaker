package com.example.playlistmaker.settings.data.impl

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.playlistmaker.settings.data.SettingsRepository

class SettingsRepositoryImpl(private val sharedPrefs: SharedPreferences) : SettingsRepository {
    override fun checkSettingsThemeMode(): Boolean {
        return (sharedPrefs.contains(THEME_MODE_KEY))
    }

    override fun saveSettingsThemeMode(set: Boolean) {
        sharedPrefs.edit { putBoolean(THEME_MODE_KEY, set) }
    }

    override fun getSettingThemMode(): Boolean {
        return sharedPrefs.getBoolean(THEME_MODE_KEY, false)
    }

    companion object {
        const val THEME_MODE_KEY = "theme_mode_key"
    }
}