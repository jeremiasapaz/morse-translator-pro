package com.morsetranslatorpro.app.playback

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import com.morsetranslatorpro.app.core.MorseElement

/**
 * Transmits Morse as a vibration pattern. Rather than toggling the motor per
 * element, it builds a single [VibrationEffect] waveform for smooth playback,
 * while still supporting stop/repeat via the base class for progress + state.
 */
class VibrationPlayer(context: Context) : AbstractElementPlayer() {

    private val vibrator: Vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val mgr = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        mgr.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    val isAvailable: Boolean get() = vibrator.hasVibrator()

    override fun play(elements: List<MorseElement>, repeat: Boolean) {
        // Build a waveform: timings alternate OFF,ON,OFF,... starting with 0 delay.
        val timings = mutableListOf<Long>(0)
        val amplitudes = mutableListOf<Int>(0)
        for (e in elements) {
            timings.add(e.durationMs)
            amplitudes.add(if (e.on) VibrationEffect.DEFAULT_AMPLITUDE else 0)
        }
        val repeatIndex = if (repeat) 0 else -1
        runCatching {
            vibrator.vibrate(
                VibrationEffect.createWaveform(timings.toLongArray(), amplitudes.toIntArray(), repeatIndex)
            )
        }
        // Delegate state/progress bookkeeping to the base loop (it won't toggle
        // the motor because onSignal is a no-op here).
        super.play(elements, repeat)
    }

    override fun onSignal(on: Boolean) { /* handled by waveform */ }

    override fun stop() {
        runCatching { vibrator.cancel() }
        super.stop()
    }
}
