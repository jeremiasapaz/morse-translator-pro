package com.morsetranslatorpro.app.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.morsetranslatorpro.app.core.MorseCode
import com.morsetranslatorpro.app.core.MorseTiming
import com.morsetranslatorpro.app.data.prefs.AppSettings
import com.morsetranslatorpro.app.data.prefs.SettingsRepository
import com.morsetranslatorpro.app.playback.AudioPlayer
import com.morsetranslatorpro.app.playback.FlashlightPlayer
import com.morsetranslatorpro.app.playback.MorsePlayer
import com.morsetranslatorpro.app.playback.PlaybackState
import com.morsetranslatorpro.app.playback.VibrationPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/** Which hardware channel a transmit screen uses. */
enum class TransmitChannel { FLASHLIGHT, AUDIO, VIBRATION }

/**
 * Drives the Flashlight / Audio / Vibration transmitter screens. Holds the text
 * being transmitted, converts to Morse, and manages the active [MorsePlayer].
 */
class TransmitViewModel(
    app: Application,
    private val settingsRepository: SettingsRepository
) : AndroidViewModel(app) {

    val flashlight by lazy { FlashlightPlayer(app) }
    val audio by lazy { AudioPlayer() }
    val vibration by lazy { VibrationPlayer(app) }

    val flashlightAvailable: Boolean get() = flashlight.isAvailable
    val vibrationAvailable: Boolean get() = vibration.isAvailable

    private val _input = MutableStateFlow("SOS")
    val input: StateFlow<String> = _input.asStateFlow()

    private val _morse = MutableStateFlow(MorseCode.encode("SOS"))
    val morse: StateFlow<String> = _morse.asStateFlow()

    private val _state = MutableStateFlow(PlaybackState.IDLE)
    val state: StateFlow<PlaybackState> = _state.asStateFlow()

    private val _progress = MutableStateFlow(0f)
    val progress: StateFlow<Float> = _progress.asStateFlow()

    private val _repeat = MutableStateFlow(false)
    val repeat: StateFlow<Boolean> = _repeat.asStateFlow()

    val settings: StateFlow<AppSettings> = settingsRepository.settings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppSettings())

    private var current: MorsePlayer? = null

    fun onInputChange(text: String) {
        _input.value = text
        _morse.value = MorseCode.encode(text)
    }

    fun toggleRepeat() { _repeat.value = !_repeat.value }

    fun setWpm(wpm: Int) = viewModelScope.launch { settingsRepository.setWpm(wpm) }
    fun setToneFrequency(hz: Int) = viewModelScope.launch { settingsRepository.setToneFrequency(hz) }
    fun setVolume(v: Float) = viewModelScope.launch { settingsRepository.setVolume(v) }

    fun play(channel: TransmitChannel) {
        val s = settings.value
        val player = when (channel) {
            TransmitChannel.FLASHLIGHT -> flashlight
            TransmitChannel.AUDIO -> audio.apply {
                frequencyHz = s.toneFrequencyHz
                volume = s.volume
            }
            TransmitChannel.VIBRATION -> vibration
        }
        current?.stop()
        current = player
        player.setOnProgress { _progress.value = it }
        player.setOnFinished {
            _state.value = PlaybackState.IDLE
            _progress.value = 0f
        }
        val elements = MorseTiming.fromWpm(s.wpm).toElements(_morse.value)
        player.play(elements, _repeat.value)
        _state.value = PlaybackState.PLAYING
    }

    fun pause() { current?.pause(); _state.value = PlaybackState.PAUSED }
    fun resume() { current?.resume(); _state.value = PlaybackState.PLAYING }
    fun stop() {
        current?.stop()
        _state.value = PlaybackState.IDLE
        _progress.value = 0f
    }

    override fun onCleared() {
        super.onCleared()
        flashlight.release()
        audio.release()
        vibration.release()
    }
}
