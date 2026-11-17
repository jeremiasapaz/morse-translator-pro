package com.morsetranslatorpro.app.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.morsetranslatorpro.app.R
import com.morsetranslatorpro.app.ui.theme.SignalOff
import com.morsetranslatorpro.app.ui.theme.SignalOn

/** A card displaying a translation result with copy + share actions. */
@Composable
fun ResultCard(
    title: String,
    content: String,
    monospace: Boolean = false,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Card(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f)) {
                androidx.compose.foundation.layout.Column {
                    Text(title, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = content.ifBlank { "—" },
                        style = MaterialTheme.typography.bodyLarge,
                        fontFamily = if (monospace) FontFamily.Monospace else FontFamily.Default
                    )
                }
            }
            IconButton(
                onClick = { if (content.isNotBlank()) ShareUtils.copyToClipboard(context, title, content) },
                modifier = Modifier.semantics { contentDescription = "Copy $title" }
            ) { Icon(Icons.Filled.ContentCopy, contentDescription = null) }
            IconButton(
                onClick = { if (content.isNotBlank()) ShareUtils.shareText(context, content) },
                modifier = Modifier.semantics { contentDescription = "Share $title" }
            ) { Icon(Icons.Filled.Share, contentDescription = null) }
        }
    }
}

/** Visualizes a Morse string as a row of animated dot/dash chips. */
@Composable
fun MorseVisualizer(morse: String, modifier: Modifier = Modifier) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(morse.length) { index ->
            when (morse[index]) {
                '.' -> Box(
                    Modifier.size(12.dp).clip(CircleShape).background(SignalOn)
                )
                '-' -> Box(
                    Modifier.width(32.dp).height(12.dp).clip(RoundedCornerShape(6.dp)).background(SignalOn)
                )
                '/' -> Spacer(Modifier.width(16.dp))
                else -> Spacer(Modifier.width(8.dp))
            }
        }
    }
}

/** A large circular indicator showing whether the signal is currently ON. */
@Composable
fun SignalIndicator(on: Boolean, modifier: Modifier = Modifier) {
    val color by animateColorAsState(if (on) SignalOn else SignalOff, label = "signal")
    Box(
        modifier = modifier
            .size(160.dp)
            .clip(CircleShape)
            .background(color)
            .semantics { contentDescription = if (on) "Signal on" else "Signal off" }
    )
}

/** Horizontal meter for live signal level (0..1). */
@Composable
fun SignalLevelBar(level: Float, modifier: Modifier = Modifier) {
    val animated by animateFloatAsState(level.coerceIn(0f, 1f), label = "level")
    LinearProgressIndicator(
        progress = { animated },
        modifier = modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp))
    )
}

/** A confidence badge colored by strength. */
@Composable
fun ConfidenceBadge(confidence: Float, modifier: Modifier = Modifier) {
    val pct = (confidence * 100).toInt()
    val color = when {
        confidence > 0.75f -> Color(0xFF2E7D32)
        confidence > 0.4f -> Color(0xFFF9A825)
        else -> Color(0xFFC62828)
    }
    Text(
        text = "Confidence: $pct%",
        style = MaterialTheme.typography.labelMedium,
        color = color,
        modifier = modifier
    )
}

@Composable
fun copyLabel() = stringResource(R.string.copy)
