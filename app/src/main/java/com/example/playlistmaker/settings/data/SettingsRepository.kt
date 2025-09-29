package com.example.playlistmaker.settings.data

interface SettingsRepository {
    fun checkSettingsThemeMode(): Boolean
    fun saveSettingsThemeMode(set: Boolean)
    fun getSettingThemMode(): Boolean
}