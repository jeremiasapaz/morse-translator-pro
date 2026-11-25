package com.morsetranslatorpro.app.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.morsetranslatorpro.app.ads.BannerAdView
import com.morsetranslatorpro.app.data.db.HistoryEntity
import com.morsetranslatorpro.app.ui.navigation.Destination
import com.morsetranslatorpro.app.ui.viewmodel.HistoryViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(viewModel: HistoryViewModel, onNavigate: (Destination) -> Unit, modifier: Modifier = Modifier) {
    val query by viewModel.query.collectAsStateWithLifecycle()
    val favoritesOnly by viewModel.favoritesOnly.collectAsStateWithLifecycle()
    val items by viewModel.items.collectAsStateWithLifecycle()
    val dateFormat = remember { SimpleDateFormat("MMM d, yyyy HH:mm", Locale.getDefault()) }

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = query,
            onValueChange = viewModel::onQueryChange,
            label = { Text("Search favorites") },
            modifier = Modifier.fillMaxWidth()
        )
        Row(Modifier.padding(vertical = 8.dp)) {
            FilterChip(
                selected = favoritesOnly,
                onClick = { viewModel.toggleFavoritesOnly() },
                label = { Text("Favorites only") }
            )
        }
        if (items.isEmpty()) {
            Text("No favorites yet.", style = MaterialTheme.typography.bodyMedium)
        }
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(items, key = { it.id }) { item ->
                HistoryRow(item, dateFormat.format(Date(item.timestamp)),
                    onFavorite = { viewModel.toggleFavorite(item) },
                    onDelete = { viewModel.delete(item) },
                    onTransmit = { destination -> onNavigate(destination) }
                )
            }
        }
        BannerAdView(modifier = Modifier.padding(top = 8.dp))
    }
}

@Composable
private fun HistoryRow(
    item: HistoryEntity,
    date: String,
    onFavorite: () -> Unit,
    onDelete: () -> Unit,
    onTransmit: (Destination) -> Unit
) {
    var showTransmitDialog by remember { mutableStateOf(false) }

    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
        Row(
            Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(item.originalText, style = MaterialTheme.typography.titleSmall)
                Text(item.morse, style = MaterialTheme.typography.bodySmall, fontFamily = FontFamily.Monospace)
                Text("$date  •  ${item.direction}", style = MaterialTheme.typography.labelSmall)
            }
            IconButton(onClick = { showTransmitDialog = true }) {
                Icon(Icons.Filled.Edit, contentDescription = "Transmit")
            }
            IconButton(onClick = onFavorite) {
                Icon(
                    if (item.isFavorite) Icons.Filled.Star else Icons.Outlined.StarBorder,
                    contentDescription = "Favorite"
                )
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete")
            }
        }
    }

    if (showTransmitDialog) {
        AlertDialog(
            onDismissRequest = { showTransmitDialog = false },
            title = { Text("Transmit via") },
            text = {
                Column {
                    Text("Choose how to transmit this message:")
                    Row(Modifier.padding(top = 16.dp)) {
                        Button(onClick = {
                            showTransmitDialog = false
                            onTransmit(Destination.FLASHLIGHT)
                        }, Modifier.weight(1f).padding(end = 4.dp)) { Text("Flashlight") }
                        Button(onClick = {
                            showTransmitDialog = false
                            onTransmit(Destination.AUDIO)
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
