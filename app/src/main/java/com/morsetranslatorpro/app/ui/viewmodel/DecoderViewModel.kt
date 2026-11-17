package com.morsetranslatorpro.app.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.morsetranslatorpro.app.core.DecodeResult
import com.morsetranslatorpro.app.data.db.HistoryEntity
import com.morsetranslatorpro.app.data.prefs.AppSettings
import com.morsetranslatorpro.app.data.prefs.SettingsRepository
import com.morsetranslatorpro.app.data.repository.HistoryRepository
import com.morsetranslatorpro.app.decoder.AudioDecoder
import com.morsetranslatorpro.app.decoder.CameraDecoder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Shared ViewModel for the Camera and Audio decoder screens. Exposes live
 * decoded text, Morse, confidence and signal level, and provides a
 * [CameraDecoder] analyzer plus start/stop control of the [AudioDecoder].
 */
class DecoderViewModel(
    app: Application,
    private val settingsRepository: SettingsRepository,
    private val historyRepository: HistoryRepository
) : AndroidViewModel(app) {

    private val _result = MutableStateFlow(DecodeResult("", "", 0f, 0))
    val result: StateFlow<DecodeResult> = _result.asStateFlow()

    private val _level = MutableStateFlow(0f)
    val level: StateFlow<Float> = _level.asStateFlow()

    private val _listening = MutableStateFlow(false)
    val listening: StateFlow<Boolean> = _listening.asStateFlow()

    val settings: StateFlow<AppSettings> = settingsRepository.settings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppSettings())

    private val audioDecoder = AudioDecoder()

    /** Creates a fresh camera analyzer wired to this ViewModel's state. */
    fun newCameraDecoder(): CameraDecoder {
        _result.value = DecodeResult("", "", 0f, 0)
        _listening.value = true
        return CameraDecoder(
            onResult = { _result.value = it },
            onLevel = { _level.value = it }
        )
    }

    fun startAudio() {
        _result.value = DecodeResult("", "", 0f, 0)
        _listening.value = true
        audioDecoder.targetFrequencyHz = settings.value.toneFrequencyHz
        audioDecoder.start(
            onResult = { _result.value = it },
            onLevel = { _level.value = it }
        )
    }

    fun stopAudio() {
        _result.value = audioDecoder.stop()
        _listening.value = false
    }

    fun stopCamera(finalResult: DecodeResult?) {
        if (finalResult != null) _result.value = finalResult
        _listening.value = false
    }

    fun saveResult() {
        val r = _result.value
        if (r.text.isBlank()) return
        viewModelScope.launch {
            val s = settings.value
            historyRepository.save(
                HistoryEntity(
                    timestamp = System.currentTimeMillis(),
                    originalText = r.text,
                    morse = r.morse,
                    direction = "DECODE",
                    wpm = s.wpm,
                    toneFrequencyHz = s.toneFrequencyHz
                ),
                isPro = s.isPro
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioDecoder.stop()
    }
}
