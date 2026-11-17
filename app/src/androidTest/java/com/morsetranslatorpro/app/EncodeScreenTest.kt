package com.morsetranslatorpro.app

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.morsetranslatorpro.app.data.db.MorseDatabase
import com.morsetranslatorpro.app.data.prefs.SettingsRepository
import com.morsetranslatorpro.app.data.repository.HistoryRepository
import com.morsetranslatorpro.app.ui.screens.EncodeScreen
import com.morsetranslatorpro.app.ui.viewmodel.TranslatorViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EncodeScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    private fun viewModel(): TranslatorViewModel {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        val db = androidx.room.Room.inMemoryDatabaseBuilder(context, MorseDatabase::class.java).build()
        return TranslatorViewModel(HistoryRepository(db.historyDao()), SettingsRepository(context))
    }

    @Test
    fun typingText_producesMorseOutput() {
        composeRule.setContent { EncodeScreen(viewModel()) }
        composeRule.onNodeWithText("Enter text").performTextInput("SOS")
        // "... --- ..." should appear in the Morse result card.
        composeRule.onNodeWithText("... --- ...").assertIsDisplayed()
    }
}
