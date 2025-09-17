package com.example.playlistmaker.domain.api

interface SettingsAppInteractor {
    fun checkSettingsThemeMode(): Boolean
    fun saveSettingsThemeMode(set: Boolean)
    fun getSettingThemMode(): Boolean
}
