package com.morsetranslatorpro.app.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AboutScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
        Text("Morse Translator Pro", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
        Text("Version 1.0.0", style = MaterialTheme.typography.labelMedium)
        Text(
            "\nA modern, fully offline Morse code toolkit. Encode text to Morse, " +
                "transmit via flashlight, audio or vibration, and decode flashing lights " +
                "or beeps in real time.\n",
            style = MaterialTheme.typography.bodyMedium
        )
        Text("International (ITU) Morse timing", style = MaterialTheme.typography.bodyMedium)
        Text("• Dot = 1 unit, Dash = 3 units", style = MaterialTheme.typography.bodySmall)
        Text("• Symbol gap = 1, Letter gap = 3, Word gap = 7", style = MaterialTheme.typography.bodySmall)
        Text(
            "\nMade with Kotlin & Jetpack Compose. Open source under the MIT license.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            "\nPrivacy: all processing happens on-device. The app works without an internet connection and does not collect personal data.",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}
