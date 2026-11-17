package com.morsetranslatorpro.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.morsetranslatorpro.app.data.db.HistoryEntity
import com.morsetranslatorpro.app.data.repository.HistoryRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class HistoryViewModel(
    private val repository: HistoryRepository
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _favoritesOnly = MutableStateFlow(false)
    val favoritesOnly: StateFlow<Boolean> = _favoritesOnly.asStateFlow()

    val items: StateFlow<List<HistoryEntity>> =
        combine3().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private fun combine3() = _favoritesOnly.flatMapLatest { favOnly ->
        _query.flatMapLatest { q ->
            when {
                favOnly -> repository.observeFavorites()
                q.isBlank() -> repository.observeAll()
                else -> repository.search(q)
            }
        }
    }

    fun onQueryChange(q: String) { _query.value = q }
    fun toggleFavoritesOnly() { _favoritesOnly.value = !_favoritesOnly.value }

    fun toggleFavorite(item: HistoryEntity) = viewModelScope.launch {
        repository.setFavorite(item.id, !item.isFavorite)
    }

    fun delete(item: HistoryEntity) = viewModelScope.launch { repository.delete(item) }
    fun clearAll() = viewModelScope.launch { repository.clearAll() }
}
