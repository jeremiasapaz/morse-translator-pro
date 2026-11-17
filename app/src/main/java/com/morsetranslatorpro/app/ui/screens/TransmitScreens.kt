package com.morsetranslatorpro.app.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.morsetranslatorpro.app.core.MorseSpeed
import com.morsetranslatorpro.app.ui.components.PlaybackControls
import com.morsetranslatorpro.app.ui.components.SignalIndicator
import com.morsetranslatorpro.app.ui.viewmodel.TransmitChannel
import com.morsetranslatorpro.app.ui.viewmodel.TransmitViewModel

@Composable
fun FlashlightScreen(viewModel: TransmitViewModel, modifier: Modifier = Modifier) {
    TransmitScaffold(
        viewModel = viewModel,
        channel = TransmitChannel.FLASHLIGHT,
        available = viewModel.flashlightAvailable,
        unavailableMessage = "No flash unit detected on this device.",
        showAudioControls = false,
        modifier = modifier
    )
}

@Composable
fun AudioScreen(viewModel: TransmitViewModel, modifier: Modifier = Modifier) {
    TransmitScaffold(
        viewModel = viewModel,
        channel = TransmitChannel.AUDIO,
        available = true,
        unavailableMessage = "",
        showAudioControls = true,
        modifier = modifier
    )
}

@Composable
private fun TransmitScaffold(
    viewModel: TransmitViewModel,
    channel: TransmitChannel,
    available: Boolean,
    unavailableMessage: String,
    showAudioControls: Boolean,
    modifier: Modifier = Modifier
) {
    val input by viewModel.input.collectAsStateWithLifecycle()
    val morse by viewModel.morse.collectAsStateWithLifecycle()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val progress by viewModel.progress.collectAsStateWithLifecycle()
    val repeat by viewModel.repeat.collectAsStateWithLifecycle()
    val settings by viewModel.settings.collectAsStateWithLifecycle()

    Column(
        modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!available && unavailableMessage.isNotEmpty()) {
            Text(unavailableMessage, color = androidx.compose.material3.MaterialTheme.colorScheme.error)
        }
        OutlinedTextField(
            value = input,
            onValueChange = viewModel::onInputChange,
            label = { Text("Message to transmit") },
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            morse.ifBlank { "—" },
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.padding(vertical = 12.dp)
        )
        if (channel != TransmitChannel.AUDIO) {
            SignalIndicator(on = state.name == "PLAYING", modifier = Modifier.padding(8.dp))
        }
        LinearProgressIndicator(progress = { progress }, modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp))
        PlaybackControls(
            state = state,
            repeat = repeat,
            onPlay = { viewModel.play(channel) },
            onPause = viewModel::pause,
            onResume = viewModel::resume,
            onStop = viewModel::stop,
            onToggleRepeat = viewModel::toggleRepeat,
            enabled = available && morse.isNotBlank()
        )

        Spacer(Modifier.height(24.dp))
        Text("Speed: ${settings.wpm} WPM", style = androidx.compose.material3.MaterialTheme.typography.titleSmall)
        Row(
            Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MorseSpeed.entries.forEach { speed ->
                FilterChip(
                    selected = settings.wpm == speed.wpm,
                    onClick = { viewModel.setWpm(speed.wpm) },
                    label = { Text(speed.label) }
                )
            }
        }
        Slider(
            value = settings.wpm.toFloat(),
            onValueChange = { viewModel.setWpm(it.toInt()) },
            valueRange = 1f..60f,
            modifier = Modifier.fillMaxWidth()
        )

        if (showAudioControls) {
            Spacer(Modifier.height(16.dp))
            Text("Tone: ${settings.toneFrequencyHz} Hz", style = androidx.compose.material3.MaterialTheme.typography.titleSmall)
            Slider(
                value = settings.toneFrequencyHz.toFloat(),
                onValueChange = { viewModel.setToneFrequency(it.toInt()) },
                valueRange = 400f..1200f,
                modifier = Modifier.fillMaxWidth()
            )
            Text("Volume: ${(settings.volume * 100).toInt()}%", style = androidx.compose.material3.MaterialTheme.typography.titleSmall)
            Slider(
                value = settings.volume,
                onValueChange = viewModel::setVolume,
                valueRange = 0f..1f,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
