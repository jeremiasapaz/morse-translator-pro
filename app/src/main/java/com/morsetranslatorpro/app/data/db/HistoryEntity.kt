package com.morsetranslatorpro.app.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * A saved translation record. Stores both the original message and the Morse
 * representation along with the playback settings in effect when saved.
 */
@Entity(tableName = "history")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long,
    val originalText: String,
    val morse: String,
    /** "ENCODE" or "DECODE" — direction of the translation. */
    val direction: String,
    val wpm: Int,
    val toneFrequencyHz: Int,
    val isFavorite: Boolean = false
)
