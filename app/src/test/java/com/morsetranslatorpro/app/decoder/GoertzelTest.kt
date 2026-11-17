package com.morsetranslatorpro.app.decoder

import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.PI
import kotlin.math.sin

class GoertzelTest {

    private val sampleRate = 44100

    private fun tone(freq: Double, samples: Int, amp: Double = 0.9): ShortArray {
        val out = ShortArray(samples)
        val w = 2 * PI * freq / sampleRate
        for (i in 0 until samples) out[i] = (amp * Short.MAX_VALUE * sin(w * i)).toInt().toShort()
        return out
    }

    @Test
    fun detectsMatchingFrequency_strongly() {
        val g = Goertzel(700.0, sampleRate)
        val mag = g.magnitude(tone(700.0, 1024))
        assertTrue("expected strong response, got $mag", mag > 0.3)
    }

    @Test
    fun rejectsOffFrequency() {
        val g = Goertzel(700.0, sampleRate)
        val matched = g.magnitude(tone(700.0, 2048))
        val off = g.magnitude(tone(1500.0, 2048))
        assertTrue("off-frequency ($off) should be much weaker than matched ($matched)", off < matched / 2)
    }

    @Test
    fun silenceProducesLowMagnitude() {
        val g = Goertzel(700.0, sampleRate)
        val mag = g.magnitude(ShortArray(1024))
        assertTrue(mag < 0.01)
    }
}
