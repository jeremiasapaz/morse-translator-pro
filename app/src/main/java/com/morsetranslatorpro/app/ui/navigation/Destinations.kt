package com.morsetranslatorpro.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material.icons.filled.Headset
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Translate
import androidx.compose.ui.graphics.vector.ImageVector

/** All navigable screens with their route and display metadata. */
enum class Destination(
    val route: String,
    val title: String,
    val icon: ImageVector,
    val inDrawer: Boolean = true
) {
    HOME("home", "Home", Icons.Filled.Home),
    ENCODE("encode", "Text to Morse", Icons.Filled.Translate),
    DECODE("decode", "Morse to Text", Icons.Filled.SwapHoriz),
    FLASHLIGHT("flashlight", "Flashlight Transmitter", Icons.Filled.FlashlightOn),
    AUDIO("audio", "Audio Transmitter", Icons.AutoMirrored.Filled.VolumeUp),
    CAMERA_DECODE("camera_decode", "Camera Decoder", Icons.Filled.CameraAlt),
    AUDIO_DECODE("audio_decode", "Audio Decoder", Icons.Filled.Headset),
    HISTORY("history", "History", Icons.Filled.History),
    SETTINGS("settings", "Settings", Icons.Filled.Settings),
    ABOUT("about", "About", Icons.Filled.Info);

    companion object {
        fun fromRoute(route: String?): Destination =
            entries.firstOrNull { it.route == route } ?: HOME
    }
}
