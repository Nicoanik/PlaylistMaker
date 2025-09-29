package com.example.playlistmaker.settings.domain.impl

import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.SettingsRepository

class SettingsInteractorImpl(private val repository: SettingsRepository) : SettingsInteractor {
    override fun checkSettingsThemeMode(): Boolean {
        return repository.checkSettingsThemeMode()
    }

    override fun saveSettingsThemeMode(set: Boolean) {
        repository.saveSettingsThemeMode(set)
    }

    override fun getSettingThemMode(): Boolean {
        return repository.getSettingThemMode()
    }
}