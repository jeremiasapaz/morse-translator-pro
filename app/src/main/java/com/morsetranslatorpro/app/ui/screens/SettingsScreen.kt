package com.morsetranslatorpro.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.morsetranslatorpro.app.data.prefs.ThemeMode
import com.morsetranslatorpro.app.ui.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(viewModel: SettingsViewModel, modifier: Modifier = Modifier) {
    val settings by viewModel.settings.collectAsStateWithLifecycle()

    Column(modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
        SectionTitle("Appearance")
        Text("Theme", style = MaterialTheme.typography.bodyMedium)
        Row(Modifier.padding(vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ThemeMode.entries.forEach { mode ->
                FilterChip(
                    selected = settings.themeMode == mode,
                    onClick = { viewModel.setTheme(mode) },
                    label = { Text(mode.name.lowercase().replaceFirstChar { it.uppercase() }) }
                )
            }
        }
        SettingSwitch("Large text", settings.largeText, viewModel::setLargeText)

        HorizontalDivider(Modifier.padding(vertical = 12.dp))
        SectionTitle("Accessibility")
        SettingSwitch("Haptic feedback", settings.hapticFeedback, viewModel::setHaptic)

        HorizontalDivider(Modifier.padding(vertical = 12.dp))
        SectionTitle("Pro")
        Text(
            if (settings.isPro) "Pro features are unlocked. Thank you!"
            else "Upgrade to Pro for advanced decoding, unlimited history and more.",
            style = MaterialTheme.typography.bodyMedium
        )
        SettingSwitch("Pro (debug toggle)", settings.isPro, viewModel::setPro)
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(text, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(bottom = 8.dp))
}

@Composable
private fun SettingSwitch(label: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(
        Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, Modifier.weight(1f), style = MaterialTheme.typography.bodyLarge)
        Switch(checked = checked, onCheckedChange = onChange)
    }
}
