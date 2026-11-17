package com.morsetranslatorpro.app.decoder

import kotlin.math.cos
import kotlin.math.sqrt

/**
 * Goertzel algorithm: efficiently computes the energy of a single target
 * frequency within a block of PCM samples. Used by the microphone decoder to
 * detect Morse beeps while rejecting broadband background noise.
 *
 * Pure DSP with no Android dependencies, so it is unit-testable on the JVM.
 */
class Goertzel(
    private val targetFrequencyHz: Double,
    private val sampleRate: Int
) {
    /**
     * @return normalized magnitude (0..~1) of [targetFrequencyHz] in [samples].
     */
    fun magnitude(samples: ShortArray, size: Int = samples.size): Double {
        if (size <= 0) return 0.0
        val k = (0.5 + (size * targetFrequencyHz) / sampleRate).toInt()
        val omega = (2.0 * Math.PI * k) / size
        val coeff = 2.0 * cos(omega)
        var s0: Double
        var s1 = 0.0
        var s2 = 0.0
        for (i in 0 until size) {
            s0 = (samples[i] / 32768.0) + coeff * s1 - s2
            s2 = s1
            s1 = s0
        }
        val power = s1 * s1 + s2 * s2 - coeff * s1 * s2
        return sqrt(power.coerceAtLeast(0.0)) / (size / 2.0)
    }
}

/**
 * Tracks a signal level over time and classifies whether the current level is
 * "on" using an adaptive threshold midway between a running noise floor and a
 * running peak. Works for both audio magnitude and camera luminance.
 */
class AdaptiveThreshold(
    private val attack: Double = 0.4,
    private val decay: Double = 0.02,
    private val marginFactor: Double = 0.5
) {
    private var floor = Double.NaN
    private var peak = Double.NaN

    fun update(level: Double): Boolean {
        if (floor.isNaN()) { floor = level; peak = level }
        // Peak tracks fast up, slow down; floor tracks slow up, fast down.
        peak = if (level > peak) level else peak * (1 - decay) + level * decay
        floor = if (level < floor) level else floor * (1 - decay) + level * decay
        val threshold = floor + (peak - floor) * marginFactor
        return level > threshold && (peak - floor) > 1e-4
    }

    fun reset() { floor = Double.NaN; peak = Double.NaN }
}
