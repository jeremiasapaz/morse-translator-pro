package com.morsetranslatorpro.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.morsetranslatorpro.app.R
import com.morsetranslatorpro.app.playback.PlaybackState

/** Reusable Play / Pause / Stop / Repeat control row for transmitter screens. */
@Composable
fun PlaybackControls(
    state: PlaybackState,
    repeat: Boolean,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onStop: () -> Unit,
    onToggleRepeat: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp, androidx.compose.ui.Alignment.CenterHorizontally),
    ) {
        FilledTonalIconButton(
            onClick = {
                when (state) {
                    PlaybackState.PLAYING -> onPause()
                    PlaybackState.PAUSED -> onResume()
                    PlaybackState.IDLE -> onPlay()
                }
            },
            enabled = enabled,
        ) {
            val icon = if (state == PlaybackState.PLAYING) Icons.Filled.Pause else Icons.Filled.PlayArrow
            val cd = if (state == PlaybackState.PLAYING) stringResource(R.string.pause) else stringResource(R.string.play)
            Icon(icon, contentDescription = cd)
        }
        FilledTonalIconButton(onClick = onStop, enabled = state != PlaybackState.IDLE) {
            Icon(Icons.Filled.Stop, contentDescription = stringResource(R.string.stop))
        }
        FilledIconToggleButton(checked = repeat, onCheckedChange = { onToggleRepeat() }) {
            Icon(Icons.Filled.Repeat, contentDescription = stringResource(R.string.repeat))
        }
    }
}
