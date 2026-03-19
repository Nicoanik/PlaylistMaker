package com.example.playlistmaker.domain.settings.impl

import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.SettingsRepository

class SettingsInteractorImpl(private val repository: SettingsRepository) : SettingsInteractor {

    override fun saveSettingsThemeMode(set: Boolean) {
        repository.saveSettingsThemeMode(set)
    }

    override fun getSettingThemMode(): Boolean {
        return repository.getSettingThemMode()
    }
}
