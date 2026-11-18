package com.morsetranslatorpro.app.screenshots

import android.Manifest
import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import com.morsetranslatorpro.app.data.db.HistoryEntity
import com.morsetranslatorpro.app.data.db.MorseDatabase
import com.morsetranslatorpro.app.data.prefs.SettingsRepository
import com.morsetranslatorpro.app.data.repository.HistoryRepository
import com.morsetranslatorpro.app.ui.screens.AboutScreen
import com.morsetranslatorpro.app.ui.screens.AudioScreen
import com.morsetranslatorpro.app.ui.screens.DecodeScreen
import com.morsetranslatorpro.app.ui.screens.EncodeScreen
import com.morsetranslatorpro.app.ui.screens.FlashlightScreen
import com.morsetranslatorpro.app.ui.screens.HistoryScreen
import com.morsetranslatorpro.app.ui.screens.HomeScreen
import com.morsetranslatorpro.app.ui.screens.SettingsScreen
import com.morsetranslatorpro.app.ui.theme.MorseTheme
import com.morsetranslatorpro.app.ui.viewmodel.HistoryViewModel
import com.morsetranslatorpro.app.ui.viewmodel.SettingsViewModel
import com.morsetranslatorpro.app.ui.viewmodel.TransmitViewModel
import com.morsetranslatorpro.app.ui.viewmodel.TranslatorViewModel
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

/**
 * Renders the app's screens on the JVM (via Robolectric + Roborazzi) and writes
 * store-ready PNG screenshots to ../marketing/screenshots. No device/emulator
 * required. Run with: ./gradlew recordRoborazziDebug
 */
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [34], qualifiers = RobolectricDeviceQualifiers.Pixel5)
class ScreenshotTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val context: Context
        get() = ApplicationProvider.getApplicationContext()

    private val outDir = "../marketing/screenshots"

    private fun newDb() =
        Room.inMemoryDatabaseBuilder(context, MorseDatabase::class.java)
            .allowMainThreadQueries().build()

    private fun translatorVm() =
        TranslatorViewModel(HistoryRepository(newDb().historyDao()), SettingsRepository(context))

    private fun capture(name: String, dark: Boolean = false, content: @Composable () -> Unit) {
        composeRule.setContent {
            MorseTheme(darkTheme = dark, dynamicColor = false) {
                Surface(modifier = Modifier.fillMaxSize()) { content() }
            }
        }
        composeRule.onRoot().captureRoboImage("$outDir/$name.png")
    }

    @Test
    fun homeScreen() = capture("01-home") {
        HomeScreen(onNavigate = {})
    }

    @Test
    fun textToMorse() = capture("02-text-to-morse") {
        val vm = translatorVm().apply { onInputChange("HELLO WORLD", encode = true) }
        EncodeScreen(vm)
    }

    @Test
    fun morseToText() = capture("03-morse-to-text") {
        val vm = translatorVm().apply { onInputChange("... --- ...", encode = false) }
        DecodeScreen(vm)
    }

    @Test
    fun flashlight() = capture("04-flashlight") {
        FlashlightScreen(TransmitViewModel(context as android.app.Application, SettingsRepository(context)))
    }

    @Test
    fun audioTransmitter() = capture("05-audio") {
        AudioScreen(TransmitViewModel(context as android.app.Application, SettingsRepository(context)))
    }

    @Test
    fun history() {
        val db = newDb()
        runBlocking {
            val dao = db.historyDao()
            dao.insert(HistoryEntity(0, System.currentTimeMillis(), "HELLO WORLD", ".... . .-.. .-.. --- / .-- --- .-. .-.. -..", "ENCODE", 15, 700, true))
            dao.insert(HistoryEntity(0, System.currentTimeMillis() - 86_400_000, "SOS", "... --- ...", "ENCODE", 20, 700, false))
            dao.insert(HistoryEntity(0, System.currentTimeMillis() - 172_800_000, "CQ DX", "-.-. --.- / -.. -..-", "DECODE", 18, 800, false))
        }
        capture("06-history") { HistoryScreen(HistoryViewModel(HistoryRepository(db.historyDao()))) }
    }

    @Test
    fun settingsLight() = capture("07-settings-light") {
        SettingsScreen(SettingsViewModel(SettingsRepository(context)))
    }

    @Test
    fun settingsDark() = capture("08-settings-dark", dark = true) {
        SettingsScreen(SettingsViewModel(SettingsRepository(context)))
    }

    @Test
    fun about() = capture("09-about") { AboutScreen() }

    @Test
    fun homeDark() = capture("10-home-dark", dark = true) {
        HomeScreen(onNavigate = {})
    }

    init {
        // Grant sensor permissions so permission-gated screens render their main UI.
        Shadows.shadowOf(
            ApplicationProvider.getApplicationContext<android.app.Application>()
        ).grantPermissions(Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA)
    }
}
