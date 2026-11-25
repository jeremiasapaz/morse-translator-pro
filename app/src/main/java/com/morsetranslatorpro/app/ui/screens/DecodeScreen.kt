package com.morsetranslatorpro.app.ui.screens

import android.app.Activity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.morsetranslatorpro.app.R
import com.morsetranslatorpro.app.ads.BannerAdView
import com.morsetranslatorpro.app.ui.components.ResultCard
import com.morsetranslatorpro.app.ui.navigation.Destination
import com.morsetranslatorpro.app.ui.viewmodel.TranslatorViewModel

/** Morse → Text screen. Accepts '.', '-', spaces and '/'. */
@Composable
fun DecodeScreen(viewModel: TranslatorViewModel, onNavigate: (Destination) -> Unit, modifier: Modifier = Modifier) {
    val input by viewModel.input.collectAsStateWithLifecycle()
    val output by viewModel.output.collectAsStateWithLifecycle()
    val activity = LocalContext.current as? Activity
    var showTransmitDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)
    ) {
        OutlinedTextField(
            value = input,
            onValueChange = { viewModel.onInputChange(it, encode = false) },
            label = { Text("Enter Morse (. - / )") },
            textStyle = androidx.compose.ui.text.TextStyle(fontFamily = FontFamily.Monospace),
            modifier = Modifier.fillMaxWidth()
        )
        Row(Modifier.padding(top = 12.dp)) {
            Button(onClick = { 
                viewModel.save(encode = false, activity)
                showTransmitDialog = true
            }) { Text("Add to Favorites") }
            OutlinedButton(onClick = { viewModel.clear() }, modifier = Modifier.padding(start = 12.dp)) {
                Text(stringResource(R.string.clear))
            }
        }
        ResultCard(
            title = "Decoded text",
            content = output,
            modifier = Modifier.padding(top = 16.dp)
        )
        BannerAdView(modifier = Modifier.padding(top = 16.dp))
    }

    if (showTransmitDialog) {
        AlertDialog(
            onDismissRequest = { showTransmitDialog = false },
            title = { Text("Transmit via") },
            text = {
                Column {
                    Text("Choose how to transmit your message:")
                    Row(Modifier.padding(top = 16.dp)) {
                        Button(onClick = {
                            showTransmitDialog = false
                            onNavigate(Destination.FLASHLIGHT)
                        }, Modifier.weight(1f).padding(end = 4.dp)) { Text("Flashlight") }
                        Button(onClick = {
                            showTransmitDialog = false
                            onNavigate(Destination.AUDIO)
                        }, Modifier.weight(1f).padding(start = 4.dp)) { Text("Audio") }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showTransmitDialog = false }) { Text("Cancel") }
            }
        )
    }
}
