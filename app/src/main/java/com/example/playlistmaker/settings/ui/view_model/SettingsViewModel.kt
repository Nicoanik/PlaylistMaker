package com.example.playlistmaker.settings.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.sharing.domain.SharingInteractor

class SettingsViewModel(
    private val settingsInteractor: SettingsInteractor,
    private val sharingInteractor: SharingInteractor
): ViewModel() {

//    private val settingsInteractor = Creator.provideSettingsInteractor()
//    private val  sharingInteractor = Creator.provideSharingInteractor()

    private val stateLiveData = MutableLiveData<Boolean>()
    fun observeState(): LiveData<Boolean> = stateLiveData

    fun changeThemeMode(checked: Boolean) {
        settingsInteractor.saveSettingsThemeMode(checked)
        stateLiveData.postValue(settingsInteractor.getSettingThemMode())
    }

    fun getSetSwitcher() {
        stateLiveData.postValue(settingsInteractor.getSettingThemMode())
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
