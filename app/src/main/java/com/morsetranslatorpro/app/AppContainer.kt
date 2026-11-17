package com.morsetranslatorpro.app

import android.content.Context
import com.morsetranslatorpro.app.billing.BillingManager
import com.morsetranslatorpro.app.data.db.MorseDatabase
import com.morsetranslatorpro.app.data.prefs.SettingsRepository
import com.morsetranslatorpro.app.data.repository.HistoryRepository

/**
 * Lightweight manual dependency container (avoids a DI framework for a small app).
 * Holds singletons scoped to the application lifetime.
 */
class AppContainer(context: Context) {
    private val appContext = context.applicationContext

    val settingsRepository: SettingsRepository by lazy { SettingsRepository(appContext) }

    val historyRepository: HistoryRepository by lazy {
        HistoryRepository(MorseDatabase.get(appContext).historyDao())
    }

    val billingManager: BillingManager by lazy {
        BillingManager(appContext, settingsRepository)
    }
}
