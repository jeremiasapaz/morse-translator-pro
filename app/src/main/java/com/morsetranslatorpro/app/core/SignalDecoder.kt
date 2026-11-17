package com.morsetranslatorpro.app.core

/** A detected pulse: [on] indicates signal present, with [durationMs]. */
data class Pulse(val on: Boolean, val durationMs: Long)

/** Result of decoding a sequence of pulses. */
data class DecodeResult(
    val morse: String,
    val text: String,
    /** Confidence 0..1 based on how cleanly pulses map to dot/dash/gap classes. */
    val confidence: Float,
    /** Estimated unit length in ms (0 if undetermined). */
    val estimatedUnitMs: Long
)

/**
 * Converts a sequence of raw ON/OFF [Pulse]s (produced by the camera or
 * microphone decoders) into Morse and then text.
 *
 * The unit length is estimated adaptively: the shortest sustained ON pulse is
 * assumed to be a dot. Classification then uses standard ITU thresholds.
 * This class contains no Android dependencies and is fully unit-testable.
 */
class SignalDecoder {

    /**
     * @param minUnitMs ignore ON pulses shorter than this (noise rejection).
     */
    fun decode(pulses: List<Pulse>, minUnitMs: Long = 20): DecodeResult {
        val clean = pulses.filter { !it.on || it.durationMs >= minUnitMs }
        val onDurations = clean.filter { it.on }.map { it.durationMs }
        if (onDurations.isEmpty()) {
            return DecodeResult("", "", 0f, 0)
        }

        // Estimate unit as the shortest ON pulse (a dot), made robust by using a
        // low percentile rather than the absolute minimum.
        val sortedOn = onDurations.sorted()
        val unit = percentile(sortedOn, 0.15).coerceAtLeast(1.0)

        val sb = StringBuilder()
        var confidenceSum = 0.0
        var confidenceCount = 0

        for (p in clean) {
            val ratio = p.durationMs / unit
            if (p.on) {
                if (ratio < 2.0) sb.append('.') else sb.append('-')
                confidenceSum += classConfidence(ratio, 1.0, 3.0)
                confidenceCount++
            } else {
                when {
                    ratio < 2.0 -> { /* intra-letter gap: no separator */ }
                    ratio < 5.0 -> sb.append(' ')
                    else -> sb.append(" / ")
                }
                if (ratio >= 2.0) {
                    confidenceSum += classConfidence(ratio, 3.0, 7.0)
                    confidenceCount++
                }
            }
        }

        val morse = sb.toString().trim().replace(Regex(" +"), " ")
        val text = MorseCode.decode(morse)
        val confidence = if (confidenceCount == 0) 0f
        else (confidenceSum / confidenceCount).toFloat().coerceIn(0f, 1f)

        return DecodeResult(morse, text, confidence, unit.toLong())
    }

    /** Confidence that [ratio] matches one of two expected class centers. */
    private fun classConfidence(ratio: Double, low: Double, high: Double): Double {
        val nearest = if (kotlin.math.abs(ratio - low) < kotlin.math.abs(ratio - high)) low else high
        val error = kotlin.math.abs(ratio - nearest) / nearest
        return (1.0 - error).coerceIn(0.0, 1.0)
    }

    private fun percentile(sorted: List<Long>, p: Double): Double {
        if (sorted.isEmpty()) return 0.0
        val idx = (p * (sorted.size - 1)).toInt().coerceIn(0, sorted.size - 1)
        return sorted[idx].toDouble()
    }
}
