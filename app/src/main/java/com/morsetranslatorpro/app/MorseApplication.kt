package com.morsetranslatorpro.app

import android.app.Application
import com.morsetranslatorpro.app.ads.AdManager

class MorseApplication : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
        AdManager.initialize(this)
    }
}
