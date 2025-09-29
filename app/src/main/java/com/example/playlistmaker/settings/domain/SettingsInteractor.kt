package com.example.playlistmaker.settings.domain

interface SettingsInteractor {
    fun checkSettingsThemeMode(): Boolean
    fun saveSettingsThemeMode(set: Boolean)
    fun getSettingThemMode(): Boolean
}
