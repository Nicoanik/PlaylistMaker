package com.example.playlistmaker.data

import android.content.SharedPreferences
import com.example.playlistmaker.domain.api.SettingsAppRepository
import androidx.core.content.edit

class SettingsAppRepositoryImpl(private val sharedPrefs: SharedPreferences) : SettingsAppRepository {
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
