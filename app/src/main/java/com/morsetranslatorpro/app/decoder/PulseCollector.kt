package com.morsetranslatorpro.app.decoder

import com.morsetranslatorpro.app.core.Pulse

/**
 * Converts a stream of boolean "signal present" samples (with timestamps) into
 * discrete [Pulse]s. Shared by the camera and microphone decoders.
 *
 * A debounce threshold suppresses spurious transitions shorter than
 * [minTransitionMs]. Thread-safety is the caller's responsibility (invoke
 * [onSample] from a single analysis thread).
 */
class PulseCollector(private val minTransitionMs: Long = 25) {

    private var currentState: Boolean? = null
    private var stateStartMs: Long = 0
    private var lastSampleMs: Long = 0
    private val pulses = mutableListOf<Pulse>()

    /** Feeds one detector sample. Returns a completed pulse if one just ended. */
    fun onSample(signalOn: Boolean, timestampMs: Long): Pulse? {
        lastSampleMs = timestampMs
        if (currentState == null) {
            currentState = signalOn
            stateStartMs = timestampMs
            return null
        }
        if (signalOn != currentState) {
            val duration = timestampMs - stateStartMs
            if (duration < minTransitionMs) {
                // too short — treat as noise, keep current state
                return null
            }
            val completed = Pulse(currentState!!, duration)
            pulses.add(completed)
            currentState = signalOn
            stateStartMs = timestampMs
            return completed
        }
        return null
    }

    /** Finalizes the in-progress pulse (e.g. when stopping) and returns all pulses. */
    fun finish(): List<Pulse> {
        currentState?.let { state ->
            val duration = lastSampleMs - stateStartMs
            if (duration >= minTransitionMs) pulses.add(Pulse(state, duration))
        }
        currentState = null
        return pulses.toList()
    }

    fun snapshot(): List<Pulse> = pulses.toList()

    fun reset() {
        currentState = null
        stateStartMs = 0
        lastSampleMs = 0
        pulses.clear()
    }
}
