package com.morsetranslatorpro.app.decoder

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.morsetranslatorpro.app.core.DecodeResult
import com.morsetranslatorpro.app.core.SignalDecoder

/**
 * Real-time camera Morse decoder. Implements [ImageAnalysis.Analyzer]; for each
 * frame it measures the average luminance of a central window (where a light
 * source is aimed), classifies on/off via [AdaptiveThreshold], converts to
 * pulses and decodes.
 */
class CameraDecoder(
    private val onResult: (DecodeResult) -> Unit,
    private val onLevel: ((Float) -> Unit)? = null
) : ImageAnalysis.Analyzer {

    private val signalDecoder = SignalDecoder()
    private val collector = PulseCollector(minTransitionMs = 60)
    private val threshold = AdaptiveThreshold(marginFactor = 0.7)

    fun reset() {
        collector.reset()
        threshold.reset()
    }

    override fun analyze(image: ImageProxy) {
        try {
            val luma = averageCenterLuminance(image)
            onLevel?.invoke((luma / 255.0).toFloat().coerceIn(0f, 1f))
            val on = threshold.update(luma)
            val completed = collector.onSample(on, System.currentTimeMillis())
            if (completed != null) {
                onResult(signalDecoder.decode(collector.snapshot()))
            }
        } finally {
            image.close()
        }
    }

    /** Stops and returns a final decode of the captured pulses. */
    fun finish(): DecodeResult = signalDecoder.decode(collector.finish())

    /**
     * Averages the Y (luminance) plane over the central 40% window of the frame.
     * YUV_420_888 plane 0 is luminance, so this is cheap and accurate.
     */
    private fun averageCenterLuminance(image: ImageProxy): Double {
        val plane = image.planes[0]
        val buffer = plane.buffer
        val rowStride = plane.rowStride
        val pixelStride = plane.pixelStride
        val width = image.width
        val height = image.height

        val xStart = (width * 0.3).toInt()
        val xEnd = (width * 0.7).toInt()
        val yStart = (height * 0.3).toInt()
        val yEnd = (height * 0.7).toInt()

        var sum = 0L
        var count = 0L
        val step = 4 // subsample for speed
        var y = yStart
        while (y < yEnd) {
            var x = xStart
            val rowBase = y * rowStride
            while (x < xEnd) {
                val index = rowBase + x * pixelStride
                if (index < buffer.limit()) {
                    sum += (buffer.get(index).toInt() and 0xFF)
                    count++
                }
                x += step
            }
            y += step
        }
        return if (count == 0L) 0.0 else sum.toDouble() / count
    }
}
