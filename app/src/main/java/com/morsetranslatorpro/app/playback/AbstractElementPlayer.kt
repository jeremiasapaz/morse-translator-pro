package com.morsetranslatorpro.app.playback

import com.morsetranslatorpro.app.core.MorseElement
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * Drives the timed element loop for players whose output is a simple ON/OFF
 * signal (flashlight, vibration). Subclasses implement [onSignal] to turn the
 * hardware on or off. Supports pause/resume/stop and progress reporting.
 */
abstract class AbstractElementPlayer : MorsePlayer {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var job: Job? = null

    @Volatile
    private var paused = false

    @Volatile
    final override var state: PlaybackState = PlaybackState.IDLE
        protected set

    private var onProgress: ((Float) -> Unit)? = null
    private var onFinished: (() -> Unit)? = null

    override fun setOnProgress(listener: ((Float) -> Unit)?) { onProgress = listener }
    override fun setOnFinished(listener: (() -> Unit)?) { onFinished = listener }

    /** Turn the hardware signal on or off. Called on a background thread. */
    protected abstract fun onSignal(on: Boolean)

    override fun play(elements: List<MorseElement>, repeat: Boolean) {
        stop()
        if (elements.isEmpty()) return
        val total = elements.sumOf { it.durationMs }.coerceAtLeast(1)
        paused = false
        state = PlaybackState.PLAYING
        job = scope.launch {
            do {
                var elapsed = 0L
                for (element in elements) {
                    if (!isActive) break
                    waitWhilePaused()
                    onSignal(element.on)
                    stepDelay(element.durationMs)
                    elapsed += element.durationMs
                    onProgress?.invoke((elapsed.toFloat() / total).coerceIn(0f, 1f))
                }
                onSignal(false)
            } while (repeat && isActive)
            if (isActive) {
                state = PlaybackState.IDLE
                onFinished?.invoke()
            }
        }
    }

    private suspend fun waitWhilePaused() {
        while (paused && (job?.isActive == true)) {
            onSignal(false)
            delay(30)
        }
    }

    /** Delays in small slices so pause/stop react quickly. */
    private suspend fun stepDelay(durationMs: Long) {
        var remaining = durationMs
        val slice = 25L
        while (remaining > 0 && (job?.isActive == true)) {
            if (paused) { waitWhilePaused(); continue }
            val d = minOf(slice, remaining)
            delay(d)
            remaining -= d
        }
    }

    override fun pause() {
        if (state == PlaybackState.PLAYING) {
            paused = true
            state = PlaybackState.PAUSED
        }
    }

    override fun resume() {
        if (state == PlaybackState.PAUSED) {
            paused = false
            state = PlaybackState.PLAYING
        }
    }

    override fun stop() {
        job?.cancel()
        job = null
        paused = false
        onSignal(false)
        state = PlaybackState.IDLE
    }

    override fun release() {
        stop()
    }
}
