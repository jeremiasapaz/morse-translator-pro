package com.morsetranslatorpro.app.core

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SignalDecoderTest {

    private val decoder = SignalDecoder()

    /** Builds a clean pulse stream from a Morse string at a given unit length. */
    private fun pulsesFor(morse: String, unit: Long): List<Pulse> {
        return MorseTiming(unit).toElements(morse).map { Pulse(it.on, it.durationMs) }
    }

    @Test
    fun decode_cleanSos_producesSos() {
        val pulses = pulsesFor("... --- ...", 100)
        val result = decoder.decode(pulses)
        assertEquals("SOS", result.text)
        assertTrue("confidence should be high", result.confidence > 0.8f)
    }

    @Test
    fun decode_cleanHello_roundTrips() {
        val pulses = pulsesFor(MorseCode.encode("HELLO"), 80)
        val result = decoder.decode(pulses)
        assertEquals("HELLO", result.text)
    }

    @Test
    fun decode_twoWords_recoversSpace() {
        val pulses = pulsesFor(MorseCode.encode("HI THERE"), 120)
        val result = decoder.decode(pulses)
        assertEquals("HI THERE", result.text)
    }

    @Test
    fun decode_withJitter_stillRecovers() {
        // Introduce +/-15% timing jitter and verify recovery.
        val base = MorseTiming(100).toElements("... --- ...")
        val jittered = base.mapIndexed { i, e ->
            val factor = if (i % 2 == 0) 1.12 else 0.9
            Pulse(e.on, (e.durationMs * factor).toLong())
        }
        val result = decoder.decode(jittered)
        assertEquals("SOS", result.text)
    }

    @Test
    fun decode_empty_returnsEmpty() {
        val result = decoder.decode(emptyList())
        assertEquals("", result.text)
        assertEquals(0f, result.confidence, 0.0001f)
    }

    @Test
    fun decode_estimatesUnitLength() {
        val pulses = pulsesFor("... --- ...", 100)
        val result = decoder.decode(pulses)
        // Shortest ON pulses (dots) are 100ms, so estimate should be close.
        assertTrue(result.estimatedUnitMs in 80..120)
    }
}
