package com.morsetranslatorpro.app.playback

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import com.morsetranslatorpro.app.core.MorseElement
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.sin

/**
 * Transmits Morse as audible beeps using a generated sine-wave tone.
 *
 * Tone frequency (400–1200 Hz) and volume are configurable. A short raised-cosine
 * envelope is applied to each ON element to avoid audible clicks.
 */
class AudioPlayer(
    @Volatile var frequencyHz: Int = 700,
    @Volatile var volume: Float = 0.8f
) : MorsePlayer {

    private val sampleRate = 44100
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var job: Job? = null
    private var track: AudioTrack? = null

    @Volatile private var paused = false
    @Volatile override var state: PlaybackState = PlaybackState.IDLE
        private set

    private var onProgress: ((Float) -> Unit)? = null
    private var onFinished: (() -> Unit)? = null

    override fun setOnProgress(listener: ((Float) -> Unit)?) { onProgress = listener }
    override fun setOnFinished(listener: (() -> Unit)?) { onFinished = listener }

    override fun play(elements: List<MorseElement>, repeat: Boolean) {
        stop()
        if (elements.isEmpty()) return
        val total = elements.sumOf { it.durationMs }.coerceAtLeast(1)
        paused = false
        state = PlaybackState.PLAYING
        job = scope.launch {
            val at = createTrack()
            track = at
            at.play()
            do {
                var elapsed = 0L
                for (element in elements) {
                    if (!isActive) break
                    while (paused && isActive) { kotlinx.coroutines.delay(30) }
                    writeElement(at, element)
                    elapsed += element.durationMs
                    onProgress?.invoke((elapsed.toFloat() / total).coerceIn(0f, 1f))
                }
            } while (repeat && isActive)
            runCatching { at.stop() }
            if (isActive) {
                state = PlaybackState.IDLE
                onFinished?.invoke()
            }
        }
    }

    private fun createTrack(): AudioTrack {
        val minBuf = AudioTrack.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        ).coerceAtLeast(sampleRate) // ~1s buffer
        return AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setSampleRate(sampleRate)
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build()
            )
            .setTransferMode(AudioTrack.MODE_STREAM)
            .setBufferSizeInBytes(minBuf)
            .build()
    }

    /** Synthesizes and writes PCM for a single element (tone if ON, silence if OFF). */
    private fun writeElement(at: AudioTrack, element: MorseElement) {
        val numSamples = (sampleRate * element.durationMs / 1000).toInt()
        if (numSamples <= 0) return
        val buffer = ShortArray(numSamples)
        if (element.on) {
            val angularFreq = 2.0 * PI * frequencyHz / sampleRate
            val rampSamples = (sampleRate * 0.005).toInt().coerceAtMost(numSamples / 2)
            val amp = (volume.coerceIn(0f, 1f) * Short.MAX_VALUE)
            for (i in 0 until numSamples) {
                var sampleAmp = amp
                if (i < rampSamples) sampleAmp *= i.toFloat() / rampSamples
                else if (i >= numSamples - rampSamples)
                    sampleAmp *= (numSamples - i).toFloat() / rampSamples
                buffer[i] = (sampleAmp * sin(angularFreq * i)).toInt().toShort()
            }
        }
        var offset = 0
        while (offset < numSamples && job?.isActive == true) {
            val written = at.write(buffer, offset, numSamples - offset)
            if (written <= 0) break
            offset += written
        }
    }

    override fun pause() {
        if (state == PlaybackState.PLAYING) { paused = true; state = PlaybackState.PAUSED; runCatching { track?.pause() } }
    }

    override fun resume() {
        if (state == PlaybackState.PAUSED) { paused = false; state = PlaybackState.PLAYING; runCatching { track?.play() } }
    }

    override fun stop() {
        job?.cancel(); job = null; paused = false
        runCatching { track?.pause(); track?.flush(); track?.stop(); track?.release() }
        track = null
        state = PlaybackState.IDLE
    }

    override fun release() { stop() }
}
