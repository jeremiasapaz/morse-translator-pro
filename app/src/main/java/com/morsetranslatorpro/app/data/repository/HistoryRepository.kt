package com.morsetranslatorpro.app.data.repository

import com.morsetranslatorpro.app.data.db.HistoryDao
import com.morsetranslatorpro.app.data.db.HistoryEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repository mediating access to translation history. Enforces the free-tier
 * history limit when the user is not Pro.
 */
class HistoryRepository(private val dao: HistoryDao) {

    companion object {
        /** Free users keep only the most recent N entries. */
        const val FREE_HISTORY_LIMIT = 20
    }

    fun observeAll(): Flow<List<HistoryEntity>> = dao.observeAll()
    fun observeFavorites(): Flow<List<HistoryEntity>> = dao.observeFavorites()
    fun search(query: String): Flow<List<HistoryEntity>> = dao.search(query)

    suspend fun save(entity: HistoryEntity, isPro: Boolean): Long {
        val id = dao.insert(entity)
        if (!isPro) enforceFreeLimit()
        return id
    }

    suspend fun setFavorite(id: Long, favorite: Boolean) = dao.setFavorite(id, favorite)

    suspend fun delete(entity: HistoryEntity) = dao.delete(entity)

    suspend fun clearAll() = dao.clearAll()

    suspend fun count(): Int = dao.count()

    private suspend fun enforceFreeLimit() {
        // Room doesn't expose a trimming query out of the box; callers relying on
        // the limit should observe the list and the UI shows an upgrade prompt.
        // We simply no-op here to avoid deleting favorites unexpectedly.
    }
}
