package com.morsetranslatorpro.app.playback

import com.morsetranslatorpro.app.core.MorseElement

/** Playback lifecycle states shared by all transmitter screens. */
enum class PlaybackState { IDLE, PLAYING, PAUSED }

/**
 * Common contract for anything that can "transmit" a sequence of timed
 * [MorseElement]s (flashlight, audio tone, vibration).
 */
interface MorsePlayer {
    /** Emits progress 0..1 as playback proceeds. Optional for a player. */
    fun setOnProgress(listener: ((Float) -> Unit)?)

    /** Called when playback finishes naturally or is stopped. */
    fun setOnFinished(listener: (() -> Unit)?)

    /** Begins transmitting the given elements. Replaces any current playback. */
    fun play(elements: List<MorseElement>, repeat: Boolean)

    fun pause()
    fun resume()
    fun stop()
    fun release()
    val state: PlaybackState
}
