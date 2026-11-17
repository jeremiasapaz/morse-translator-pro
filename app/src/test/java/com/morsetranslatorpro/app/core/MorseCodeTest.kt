package com.morsetranslatorpro.app.core

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MorseCodeTest {

    @Test
    fun encode_hello_producesCorrectMorse() {
        assertEquals(".... . .-.. .-.. ---", MorseCode.encode("HELLO"))
    }

    @Test
    fun encode_isCaseInsensitive() {
        assertEquals(MorseCode.encode("hello"), MorseCode.encode("HELLO"))
    }

    @Test
    fun encode_twoWords_usesWordSeparator() {
        assertEquals(".... .. / - .... . .-. .", MorseCode.encode("HI THERE"))
    }

    @Test
    fun encode_sos() {
        assertEquals("... --- ...", MorseCode.encode("SOS"))
    }

    @Test
    fun encode_digitsAndPunctuation() {
        assertEquals(".---- ..--- ...--", MorseCode.encode("123"))
        assertEquals("--..--", MorseCode.encode(","))
    }

    @Test
    fun encode_blank_returnsEmpty() {
        assertEquals("", MorseCode.encode(""))
        assertEquals("", MorseCode.encode("   "))
    }

    @Test
    fun encode_unknownChar_usesPlaceholder() {
        assertTrue(MorseCode.encode("~").contains(MorseCode.UNKNOWN))
    }

    @Test
    fun decode_roundTrip_simple() {
        assertEquals("HELLO", MorseCode.decode(".... . .-.. .-.. ---"))
    }

    @Test
    fun decode_withWordSeparator() {
        assertEquals("HI THERE", MorseCode.decode(".... .. / - .... . .-. ."))
    }

    @Test
    fun decode_toleratesSlashWithoutSpaces() {
        assertEquals("HI THERE", MorseCode.decode(".... ../- .... . .-. ."))
    }

    @Test
    fun roundTrip_variousMessages() {
        for (msg in listOf("SOS", "THE QUICK BROWN FOX", "ABC 123", "HELLO WORLD")) {
            val encoded = MorseCode.encode(msg)
            assertEquals(msg, MorseCode.decode(encoded))
        }
    }

    @Test
    fun isFullyEncodable_detectsUnsupported() {
        assertTrue(MorseCode.isFullyEncodable("HELLO WORLD"))
        assertFalse(MorseCode.isFullyEncodable("hello~"))
    }

    @Test
    fun isValidMorse_checks() {
        assertTrue(MorseCode.isValidMorse(".... . .-.. .-.. ---"))
        assertFalse(MorseCode.isValidMorse("hello"))
    }
}
