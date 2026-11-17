package com.morsetranslatorpro.app.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.morsetranslatorpro.app.AppContainer

/**
 * Builds ViewModels, injecting dependencies from the [AppContainer]. Used in
 * place of a DI framework to keep the project lightweight.
 */
class ViewModelFactory(
    private val app: Application,
    private val container: AppContainer
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(TranslatorViewModel::class.java) ->
            TranslatorViewModel(container.historyRepository, container.settingsRepository) as T
        modelClass.isAssignableFrom(TransmitViewModel::class.java) ->
            TransmitViewModel(app, container.settingsRepository) as T
        modelClass.isAssignableFrom(DecoderViewModel::class.java) ->
            DecoderViewModel(app, container.settingsRepository, container.historyRepository) as T
        modelClass.isAssignableFrom(HistoryViewModel::class.java) ->
            HistoryViewModel(container.historyRepository) as T
        modelClass.isAssignableFrom(SettingsViewModel::class.java) ->
            SettingsViewModel(container.settingsRepository) as T
        else -> throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
    }
}
