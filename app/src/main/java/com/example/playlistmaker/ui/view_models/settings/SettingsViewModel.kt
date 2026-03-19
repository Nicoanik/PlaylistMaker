package com.example.playlistmaker.ui.view_models.settings

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.sharing.SharingInteractor
import kotlinx.coroutines.flow.MutableStateFlow

class SettingsViewModel(
    private val settingsInteractor: SettingsInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {

    private val _isChecked = MutableStateFlow(false)
    val isChecked = _isChecked

    init {
        getSetSwitcher()
    }

    fun changeThemeMode(checked: Boolean) {
        settingsInteractor.saveSettingsThemeMode(checked)
        getSetSwitcher()
    }

    fun getSetSwitcher() {
        _isChecked.value = settingsInteractor.getSettingThemMode()
    }

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun openTerms() {
        sharingInteractor.openTerms()
    }

    fun openSupport() {
        sharingInteractor.openSupport()
    }

}