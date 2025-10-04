package com.example.playlistmaker.settings.domain

interface SettingsInteractor {
    fun saveSettingsThemeMode(set: Boolean)
    fun getSettingThemMode(): Boolean
}
