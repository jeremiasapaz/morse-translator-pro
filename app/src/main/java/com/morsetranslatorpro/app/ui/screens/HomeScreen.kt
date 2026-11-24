package com.morsetranslatorpro.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.morsetranslatorpro.app.ads.BannerAdView
import com.morsetranslatorpro.app.ui.navigation.Destination

/** Home hub with a feature grid linking to each tool. */
@Composable
fun HomeScreen(onNavigate: (Destination) -> Unit, modifier: Modifier = Modifier) {
    val features = listOf(
        Destination.ENCODE, Destination.DECODE,
        Destination.FLASHLIGHT, Destination.AUDIO,
        Destination.CAMERA_DECODE, Destination.AUDIO_DECODE,
        Destination.HISTORY, Destination.SETTINGS
    )
    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Text(
            "Morse Translator Pro",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            "Encode, transmit and decode Morse code — fully offline.",
            style = MaterialTheme.typography.bodyMedium
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxWidth().weight(1f).padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(features) { dest ->
                Card(onClick = { onNavigate(dest) }, modifier = Modifier.fillMaxWidth().height(120.dp)) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(dest.icon, contentDescription = null, modifier = Modifier.size(40.dp), tint = MaterialTheme.colorScheme.primary)
                        Row(Modifier.height(8.dp)) {}
                        Text(dest.title, style = MaterialTheme.typography.titleSmall, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                    }
                }
            }
        }
        BannerAdView(modifier = Modifier.padding(top = 16.dp))
    }
}
