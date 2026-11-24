package com.morsetranslatorpro.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.morsetranslatorpro.app.ads.AdManager
import com.morsetranslatorpro.app.data.prefs.AppSettings
import com.morsetranslatorpro.app.data.prefs.ThemeMode
import com.morsetranslatorpro.app.ui.MorseApp
import com.morsetranslatorpro.app.ui.theme.MorseTheme
import com.morsetranslatorpro.app.ui.viewmodel.ViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val container = (application as MorseApplication).container
        container.billingManager.connect()
        AdManager.loadInterstitial(this)
        val factory = ViewModelFactory(application, container)

        setContent {
            val settings by container.settingsRepository.settings
                .collectAsStateWithLifecycle(initialValue = AppSettings())
            val dark = when (settings.themeMode) {
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
                ThemeMode.SYSTEM -> androidx.compose.foundation.isSystemInDarkTheme()
            }
            MorseTheme(darkTheme = dark, largeText = settings.largeText) {
                MorseApp(factory)
            }
        }
    }
}
