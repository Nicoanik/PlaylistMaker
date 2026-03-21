package com.example.playlistmaker.domain.settings

interface SettingsRepository {
    fun saveSettingsThemeMode(set: Boolean)
    fun getSettingThemMode(): Boolean
}
