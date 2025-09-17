package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.SettingsAppInteractor
import com.example.playlistmaker.domain.api.SettingsAppRepository

class SettingsAppInteractorImpl(private val repository: SettingsAppRepository) : SettingsAppInteractor {
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
