package com.example.playlistmaker.domain.settings

interface SettingsInteractor {
    fun saveSettingsThemeMode(set: Boolean)
    fun getSettingThemMode(): Boolean
}
