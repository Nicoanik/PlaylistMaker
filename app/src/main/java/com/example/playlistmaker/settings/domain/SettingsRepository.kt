package com.example.playlistmaker.settings.domain

interface SettingsRepository {
    fun saveSettingsThemeMode(set: Boolean)
    fun getSettingThemMode(): Boolean
}