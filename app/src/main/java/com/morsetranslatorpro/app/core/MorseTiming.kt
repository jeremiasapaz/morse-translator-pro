package com.morsetranslatorpro.app.core

/**
 * Represents one timed element of a Morse transmission.
 * @param on true for a signal (light/tone/vibration ON), false for a gap.
 * @param durationMs duration of this element in milliseconds.
 */
data class MorseElement(val on: Boolean, val durationMs: Long)

/**
 * Converts Morse strings into precise timed sequences following ITU timing.
 *
 * Timing units (where 1 unit = [unitMillis]):
 *  - dot = 1 unit (ON)
 *  - dash = 3 units (ON)
 *  - gap between symbols in a letter = 1 unit (OFF)
 *  - gap between letters = 3 units (OFF)
 *  - gap between words = 7 units (OFF)
 *
 * WPM uses the PARIS standard: unit(ms) = 1200 / WPM.
 */
class MorseTiming(val unitMillis: Long) {

    companion object {
        const val DEFAULT_WPM = 15
        const val MIN_WPM = 1
        const val MAX_WPM = 60

        /** Unit duration in ms for a given words-per-minute using PARIS standard. */
        fun unitMillisForWpm(wpm: Int): Long {
            val clamped = wpm.coerceIn(MIN_WPM, MAX_WPM)
            return (1200.0 / clamped).toLong()
        }

        fun fromWpm(wpm: Int): MorseTiming = MorseTiming(unitMillisForWpm(wpm))
    }

    /**
     * Expands a Morse string (dots/dashes/spaces/slashes) into an ordered list
     * of [MorseElement]s. No leading or trailing gap is emitted.
     */
    fun toElements(morse: String): List<MorseElement> {
        val elements = mutableListOf<MorseElement>()
        val normalized = morse.trim().replace(Regex("\\s*/\\s*"), "/")
        val words = normalized.split("/").filter { it.isNotBlank() }

        words.forEachIndexed { wIndex, word ->
            if (wIndex > 0) elements.add(MorseElement(false, unitMillis * 7))
            val letters = word.trim().split(Regex(" +")).filter { it.isNotEmpty() }
            letters.forEachIndexed { lIndex, letter ->
                if (lIndex > 0) elements.add(MorseElement(false, unitMillis * 3))
                letter.forEachIndexed { sIndex, symbol ->
                    if (sIndex > 0) elements.add(MorseElement(false, unitMillis))
                    when (symbol) {
                        '.' -> elements.add(MorseElement(true, unitMillis))
                        '-' -> elements.add(MorseElement(true, unitMillis * 3))
                    }
                }
            }
        }
        return elements
    }

    /** Total duration in ms of transmitting [morse]. */
    fun totalDuration(morse: String): Long = toElements(morse).sumOf { it.durationMs }
}

/** Convenience speed presets exposed in the UI. */
enum class MorseSpeed(val label: String, val wpm: Int) {
    SLOW("Slow", 8),
    MEDIUM("Medium", 15),
    FAST("Fast", 25);

    companion object {
        fun default() = MEDIUM
    }
}
