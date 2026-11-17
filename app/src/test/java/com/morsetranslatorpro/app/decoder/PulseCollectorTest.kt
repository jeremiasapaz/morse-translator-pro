package com.morsetranslatorpro.app.decoder

import com.morsetranslatorpro.app.core.MorseCode
import com.morsetranslatorpro.app.core.SignalDecoder
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class PulseCollectorTest {

    @Test
    fun emitsPulseOnTransition() {
        val c = PulseCollector(minTransitionMs = 10)
        assertNull(c.onSample(true, 0))
        assertNull(c.onSample(true, 50))
        val pulse = c.onSample(false, 100)
        assertEquals(true, pulse?.on)
        assertEquals(100L, pulse?.durationMs)
    }

    @Test
    fun suppressesShortGlitches() {
        val c = PulseCollector(minTransitionMs = 30)
        c.onSample(true, 0)
        // glitch to false for only 5ms, should be ignored
        val glitch = c.onSample(false, 5)
        assertNull(glitch)
    }

    @Test
    fun endToEnd_simulatedFlashes_decodeToSos() {
        // Simulate a camera/mic producing on/off samples for S O S at unit=100ms.
        val c = PulseCollector(minTransitionMs = 20)
        val timing = com.morsetranslatorpro.app.core.MorseTiming(100)
        var t = 0L
        val sampleStep = 10L
        for (element in timing.toElements("... --- ...")) {
            val end = t + element.durationMs
            while (t < end) {
                c.onSample(element.on, t)
                t += sampleStep
            }
        }
        val pulses = c.finish()
        val result = SignalDecoder().decode(pulses)
        assertEquals("SOS", result.text)
        assertEquals(MorseCode.encode("SOS"), result.morse)
    }
}
