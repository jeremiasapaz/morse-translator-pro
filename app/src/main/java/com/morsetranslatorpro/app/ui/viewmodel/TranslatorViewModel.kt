package com.morsetranslatorpro.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.morsetranslatorpro.app.core.MorseCode
import com.morsetranslatorpro.app.data.db.HistoryEntity
import com.morsetranslatorpro.app.data.prefs.AppSettings
import com.morsetranslatorpro.app.data.prefs.SettingsRepository
import com.morsetranslatorpro.app.data.repository.HistoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Backs both the Text→Morse and Morse→Text screens. Converts input reactively
 * and can persist results to history.
 */
class TranslatorViewModel(
    private val historyRepository: HistoryRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _input = MutableStateFlow("")
    val input: StateFlow<String> = _input.asStateFlow()

    private val _output = MutableStateFlow("")
    val output: StateFlow<String> = _output.asStateFlow()

    val settings: StateFlow<AppSettings> = settingsRepository.settings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppSettings())

    fun onInputChange(text: String, encode: Boolean) {
        _input.value = text
        _output.value = if (encode) MorseCode.encode(text) else MorseCode.decode(text)
    }

    fun clear() {
        _input.value = ""
        _output.value = ""
    }

    fun save(encode: Boolean) {
        val original = if (encode) _input.value else _output.value
        val morse = if (encode) _output.value else _input.value
        if (original.isBlank() || morse.isBlank()) return
        viewModelScope.launch {
            val s = settings.value
            historyRepository.save(
                HistoryEntity(
                    timestamp = System.currentTimeMillis(),
                    originalText = original,
                    morse = morse,
                    direction = if (encode) "ENCODE" else "DECODE",
                    wpm = s.wpm,
                    toneFrequencyHz = s.toneFrequencyHz
                ),
                isPro = s.isPro
            )
        }
    }
}
