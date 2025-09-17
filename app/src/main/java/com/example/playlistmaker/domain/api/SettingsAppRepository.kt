package com.example.playlistmaker.domain.api

interface SettingsAppRepository {
    fun checkSettingsThemeMode(): Boolean
    fun saveSettingsThemeMode(set: Boolean)
    fun getSettingThemMode(): Boolean
}
