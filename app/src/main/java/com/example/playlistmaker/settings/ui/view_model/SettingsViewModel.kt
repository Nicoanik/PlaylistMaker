package com.example.playlistmaker.settings.ui.view_model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.App
import com.example.playlistmaker.creator.Creator

class SettingsViewModel(context: Context): ViewModel() {

    private val settingsInteractor = Creator.provideSettingsInteractor()

    private val stateLiveData = MutableLiveData<Boolean>()
    fun observeState(): LiveData<Boolean> = stateLiveData

    fun changeThemeMode(checked: Boolean) {
        settingsInteractor.saveSettingsThemeMode(checked)
        stateLiveData.postValue(settingsInteractor.getSettingThemMode())
    }

    fun getSetSwitcher() {
        stateLiveData.postValue(settingsInteractor.getSettingThemMode())
    }

    companion object {
        fun getFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as App)
                SettingsViewModel(app)
            }
        }
    }
}
