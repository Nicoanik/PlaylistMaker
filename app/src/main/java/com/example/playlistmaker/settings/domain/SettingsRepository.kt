package com.example.playlistmaker.settings.domain

interface SettingsRepository {
    fun checkSettingsThemeMode(): Boolean
    fun saveSettingsThemeMode(set: Boolean)
    fun getSettingThemMode(): Boolean
}