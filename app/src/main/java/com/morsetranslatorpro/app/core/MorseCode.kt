package com.morsetranslatorpro.app.core

/**
 * International Morse Code (ITU) encoder/decoder.
 *
 * Encoding rules used for the string representation:
 *  - "."  dot
 *  - "-"  dash
 *  - " "  (single space) separates letters within a word
 *  - "/"  (slash, surrounded by spaces) separates words
 *
 * The engine is pure/deterministic and has no Android dependencies so it can be
 * unit-tested on the JVM.
 */
object MorseCode {

    /** Character -> Morse pattern (ITU international standard incl. punctuation). */
    val CHAR_TO_MORSE: Map<Char, String> = linkedMapOf(
        'A' to ".-", 'B' to "-...", 'C' to "-.-.", 'D' to "-..", 'E' to ".",
        'F' to "..-.", 'G' to "--.", 'H' to "....", 'I' to "..", 'J' to ".---",
        'K' to "-.-", 'L' to ".-..", 'M' to "--", 'N' to "-.", 'O' to "---",
        'P' to ".--.", 'Q' to "--.-", 'R' to ".-.", 'S' to "...", 'T' to "-",
        'U' to "..-", 'V' to "...-", 'W' to ".--", 'X' to "-..-", 'Y' to "-.--",
        'Z' to "--..",
        '0' to "-----", '1' to ".----", '2' to "..---", '3' to "...--",
        '4' to "....-", '5' to ".....", '6' to "-....", '7' to "--...",
        '8' to "---..", '9' to "----.",
        '.' to ".-.-.-", ',' to "--..--", '?' to "..--..", '\'' to ".----.",
        '!' to "-.-.--", '/' to "-..-.", '(' to "-.--.", ')' to "-.--.-",
        '&' to ".-...", ':' to "---...", ';' to "-.-.-.", '=' to "-...-",
        '+' to ".-.-.", '-' to "-....-", '_' to "..--.-", '"' to ".-..-.",
        '$' to "...-..-", '@' to ".--.-.",
        // Common accented / extended letters
        'Ä' to ".-.-", 'Á' to ".--.-", 'É' to "..-..", 'Ñ' to "--.--",
        'Ö' to "---.", 'Ü' to "..--"
    )

    /** Reverse lookup: Morse pattern -> character. */
    val MORSE_TO_CHAR: Map<String, Char> =
        CHAR_TO_MORSE.entries.associate { (k, v) -> v to k }

    const val LETTER_SEPARATOR = " "
    const val WORD_SEPARATOR = " / "

    /** Characters that cannot be represented are rendered as this token. */
    const val UNKNOWN = "#"

    /**
     * Encodes arbitrary text into a Morse string.
     * Letters are separated by a single space, words by " / ".
     * Unsupported characters are replaced by [UNKNOWN].
     */
    fun encode(text: String): String {
        if (text.isBlank()) return ""
        return text.trim()
            .uppercase()
            .split(Regex("\\s+"))
            .joinToString(WORD_SEPARATOR) { word ->
                word.map { ch -> CHAR_TO_MORSE[ch] ?: UNKNOWN }
                    .joinToString(LETTER_SEPARATOR)
            }
    }

    /**
     * Decodes a Morse string back into text. Accepts both " / " and "/" (with
     * optional surrounding spaces) as word separators and collapses extra
     * whitespace. Unknown patterns become [UNKNOWN].
     */
    fun decode(morse: String): String {
        if (morse.isBlank()) return ""
        val normalized = morse.trim().replace(Regex("\\s*/\\s*"), " / ")
        return normalized.split(" / ").joinToString(" ") { word ->
            word.trim().split(Regex(" +"))
                .filter { it.isNotEmpty() }
                .map { symbol -> MORSE_TO_CHAR[symbol]?.toString() ?: UNKNOWN }
                .joinToString("")
        }.trim()
    }

    /** True if every non-space character in [text] can be encoded. */
    fun isFullyEncodable(text: String): Boolean =
        text.uppercase().all { it.isWhitespace() || CHAR_TO_MORSE.containsKey(it) }

    /** Returns true if [morse] only contains valid morse symbols/separators. */
    fun isValidMorse(morse: String): Boolean =
        morse.isBlank() || morse.all { it == '.' || it == '-' || it == ' ' || it == '/' }
}
