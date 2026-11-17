package com.morsetranslatorpro.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.morsetranslatorpro.app.data.prefs.AppSettings
import com.morsetranslatorpro.app.data.prefs.SettingsRepository
import com.morsetranslatorpro.app.data.prefs.ThemeMode
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val repository: SettingsRepository
) : ViewModel() {

    val settings: StateFlow<AppSettings> = repository.settings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppSettings())

    fun setTheme(mode: ThemeMode) = viewModelScope.launch { repository.setThemeMode(mode) }
    fun setWpm(wpm: Int) = viewModelScope.launch { repository.setWpm(wpm) }
    fun setTone(hz: Int) = viewModelScope.launch { repository.setToneFrequency(hz) }
    fun setVolume(v: Float) = viewModelScope.launch { repository.setVolume(v) }
    fun setHaptic(on: Boolean) = viewModelScope.launch { repository.setHaptic(on) }
    fun setLargeText(on: Boolean) = viewModelScope.launch { repository.setLargeText(on) }
    fun setPro(on: Boolean) = viewModelScope.launch { repository.setPro(on) }
}
