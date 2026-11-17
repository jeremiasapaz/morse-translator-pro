package com.morsetranslatorpro.app.decoder

import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import com.morsetranslatorpro.app.core.DecodeResult
import com.morsetranslatorpro.app.core.SignalDecoder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * Real-time microphone Morse decoder.
 *
 * Captures PCM audio, measures the energy at the target tone frequency using
 * [Goertzel], applies an [AdaptiveThreshold] for noise rejection, converts the
 * on/off stream to pulses via [PulseCollector], and decodes with [SignalDecoder].
 */
class AudioDecoder(
    var targetFrequencyHz: Int = 700,
    private val sampleRate: Int = 44100
) {
    private val scope = CoroutineScope(Dispatchers.Default)
    private var job: Job? = null

    private val signalDecoder = SignalDecoder()
    private val collector = PulseCollector(minTransitionMs = 30)
    private val threshold = AdaptiveThreshold()

    /** @return true while actively listening. */
    val isRunning: Boolean get() = job?.isActive == true

    /**
     * Starts listening. [onResult] is invoked on a background thread whenever
     * the decoded text changes. Requires RECORD_AUDIO permission.
     */
    @SuppressLint("MissingPermission")
    fun start(onResult: (DecodeResult) -> Unit, onLevel: ((Float) -> Unit)? = null) {
        stop()
        collector.reset()
        threshold.reset()
        val blockSize = (sampleRate * 0.03).toInt() // 30ms blocks
        val minBuf = AudioRecord.getMinBufferSize(
            sampleRate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT
        ).coerceAtLeast(blockSize * 2)

        job = scope.launch {
            val record = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                sampleRate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                minBuf
            )
            val goertzel = Goertzel(targetFrequencyHz.toDouble(), sampleRate)
            val buffer = ShortArray(blockSize)
            runCatching { record.startRecording() }
            try {
                while (isActive) {
                    val read = record.read(buffer, 0, blockSize)
                    if (read <= 0) continue
                    val now = System.currentTimeMillis()
                    val mag = goertzel.magnitude(buffer, read)
                    onLevel?.invoke(mag.toFloat().coerceIn(0f, 1f))
                    val on = threshold.update(mag)
                    val completed = collector.onSample(on, now)
                    if (completed != null) {
                        onResult(signalDecoder.decode(collector.snapshot()))
                    }
                }
            } finally {
                runCatching { record.stop(); record.release() }
            }
        }
    }

    /** Stops listening and returns the final decode of everything captured. */
    fun stop(): DecodeResult {
        job?.cancel(); job = null
        return signalDecoder.decode(collector.finish())
    }
}
