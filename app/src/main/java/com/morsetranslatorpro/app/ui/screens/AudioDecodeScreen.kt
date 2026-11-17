package com.morsetranslatorpro.app.ui.screens

import android.Manifest
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.morsetranslatorpro.app.ui.components.ConfidenceBadge
import com.morsetranslatorpro.app.ui.components.ResultCard
import com.morsetranslatorpro.app.ui.components.SignalLevelBar
import com.morsetranslatorpro.app.ui.viewmodel.DecoderViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AudioDecodeScreen(viewModel: DecoderViewModel, modifier: Modifier = Modifier) {
    val micPermission = rememberPermissionState(Manifest.permission.RECORD_AUDIO)
    val result by viewModel.result.collectAsStateWithLifecycle()
    val level by viewModel.level.collectAsStateWithLifecycle()
    val listening by viewModel.listening.collectAsStateWithLifecycle()

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        if (!micPermission.status.isGranted) {
            Text("Microphone permission is required to decode Morse beeps.")
            Button(onClick = { micPermission.launchPermissionRequest() }, modifier = Modifier.padding(top = 12.dp)) {
                Text("Grant microphone permission")
            }
            return@Column
        }

        Text("Listening for Morse beeps at your configured tone frequency.", style = MaterialTheme.typography.bodyMedium)
        SignalLevelBar(level, modifier = Modifier.padding(vertical = 16.dp))

        Button(
            onClick = { if (listening) viewModel.stopAudio() else viewModel.startAudio() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(if (listening) Icons.Filled.Stop else Icons.Filled.Mic, contentDescription = null)
            Text(if (listening) "  Stop listening" else "  Start listening")
        }

        ResultCard(title = "Decoded text", content = result.text, modifier = Modifier.padding(top = 16.dp))
        ConfidenceBadge(result.confidence, modifier = Modifier.padding(top = 8.dp))
        Text(result.morse, modifier = Modifier.padding(top = 8.dp))
        Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = { viewModel.saveResult() }, enabled = result.text.isNotBlank()) { Text("Save") }
        }
    }
}
