package com.morsetranslatorpro.app.core

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MorseTimingTest {

    @Test
    fun unitMillis_followsParisStandard() {
        assertEquals(100L, MorseTiming.unitMillisForWpm(12))
        assertEquals(60L, MorseTiming.unitMillisForWpm(20))
    }

    @Test
    fun wpm_isClamped() {
        assertEquals(MorseTiming.unitMillisForWpm(MorseTiming.MAX_WPM), MorseTiming.unitMillisForWpm(999))
        assertEquals(MorseTiming.unitMillisForWpm(MorseTiming.MIN_WPM), MorseTiming.unitMillisForWpm(0))
    }

    @Test
    fun toElements_singleE_isOneDot() {
        val timing = MorseTiming(100)
        val elements = timing.toElements(".")
        assertEquals(1, elements.size)
        assertEquals(MorseElement(true, 100), elements[0])
    }

    @Test
    fun toElements_dash_isThreeUnits() {
        val timing = MorseTiming(100)
        val elements = timing.toElements("-")
        assertEquals(listOf(MorseElement(true, 300)), elements)
    }

    @Test
    fun toElements_letterA_hasIntraGap() {
        val timing = MorseTiming(100)
        // A = ".-" => dot(100) gap(100) dash(300)
        val elements = timing.toElements(".-")
        assertEquals(
            listOf(
                MorseElement(true, 100),
                MorseElement(false, 100),
                MorseElement(true, 300)
            ),
            elements
        )
    }

    @Test
    fun toElements_twoLetters_hasLetterGap() {
        val timing = MorseTiming(100)
        // "E E" => dot, gap(3 units), dot
        val elements = timing.toElements(". .")
        assertEquals(
            listOf(
                MorseElement(true, 100),
                MorseElement(false, 300),
                MorseElement(true, 100)
            ),
            elements
        )
    }

    @Test
    fun toElements_words_haveWordGap() {
        val timing = MorseTiming(100)
        val elements = timing.toElements(". / .")
        assertTrue(elements.any { !it.on && it.durationMs == 700L })
    }

    @Test
    fun totalDuration_sumsElements() {
        val timing = MorseTiming(100)
        // ".-" => dot(100) + intra-gap(100) + dash(300) = 500
        assertEquals(500L, timing.totalDuration(".-"))
    }
}
