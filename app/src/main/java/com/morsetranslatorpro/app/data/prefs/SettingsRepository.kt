package com.morsetranslatorpro.app.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.morsetranslatorpro.app.core.MorseTiming
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "morse_settings")

/** Theme options for the app. */
enum class ThemeMode { SYSTEM, LIGHT, DARK }

/** Snapshot of all user-configurable settings. */
data class AppSettings(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val wpm: Int = MorseTiming.DEFAULT_WPM,
    val toneFrequencyHz: Int = 700,
    val volume: Float = 0.8f,
    val hapticFeedback: Boolean = true,
    val largeText: Boolean = false,
    val isPro: Boolean = false
)

/**
 * Persists user settings using Jetpack DataStore. Exposes a reactive [settings]
 * flow and suspend setters.
 */
class SettingsRepository(private val context: Context) {

    private object Keys {
        val THEME = stringPreferencesKey("theme_mode")
        val WPM = intPreferencesKey("wpm")
        val TONE = intPreferencesKey("tone_hz")
        val VOLUME = intPreferencesKey("volume_permille")
        val HAPTIC = booleanPreferencesKey("haptic")
        val LARGE_TEXT = booleanPreferencesKey("large_text")
        val PRO = booleanPreferencesKey("is_pro")
    }

    val settings: Flow<AppSettings> = context.dataStore.data.map { p ->
        AppSettings(
            themeMode = runCatching { ThemeMode.valueOf(p[Keys.THEME] ?: "SYSTEM") }
                .getOrDefault(ThemeMode.SYSTEM),
            wpm = p[Keys.WPM] ?: MorseTiming.DEFAULT_WPM,
            toneFrequencyHz = p[Keys.TONE] ?: 700,
            volume = (p[Keys.VOLUME] ?: 800) / 1000f,
            hapticFeedback = p[Keys.HAPTIC] ?: true,
            largeText = p[Keys.LARGE_TEXT] ?: false,
            isPro = p[Keys.PRO] ?: false
        )
    }

    suspend fun setThemeMode(mode: ThemeMode) =
        context.dataStore.edit { it[Keys.THEME] = mode.name }

    suspend fun setWpm(wpm: Int) =
        context.dataStore.edit { it[Keys.WPM] = wpm.coerceIn(MorseTiming.MIN_WPM, MorseTiming.MAX_WPM) }

    suspend fun setToneFrequency(hz: Int) =
        context.dataStore.edit { it[Keys.TONE] = hz.coerceIn(400, 1200) }

    suspend fun setVolume(volume: Float) =
        context.dataStore.edit { it[Keys.VOLUME] = (volume.coerceIn(0f, 1f) * 1000).toInt() }

    suspend fun setHaptic(enabled: Boolean) =
        context.dataStore.edit { it[Keys.HAPTIC] = enabled }

    suspend fun setLargeText(enabled: Boolean) =
        context.dataStore.edit { it[Keys.LARGE_TEXT] = enabled }

    suspend fun setPro(isPro: Boolean) =
        context.dataStore.edit { it[Keys.PRO] = isPro }
}
