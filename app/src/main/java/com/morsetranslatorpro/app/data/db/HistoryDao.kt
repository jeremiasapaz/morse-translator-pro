package com.morsetranslatorpro.app.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {

    @Query("SELECT * FROM history ORDER BY timestamp DESC")
    fun observeAll(): Flow<List<HistoryEntity>>

    @Query("SELECT * FROM history WHERE isFavorite = 1 ORDER BY timestamp DESC")
    fun observeFavorites(): Flow<List<HistoryEntity>>

    @Query(
        "SELECT * FROM history WHERE originalText LIKE '%' || :query || '%' " +
            "OR morse LIKE '%' || :query || '%' ORDER BY timestamp DESC"
    )
    fun search(query: String): Flow<List<HistoryEntity>>

    @Query("SELECT * FROM history WHERE id = :id")
    suspend fun getById(id: Long): HistoryEntity?

    @Insert
    suspend fun insert(entity: HistoryEntity): Long

    @Update
    suspend fun update(entity: HistoryEntity)

    @Query("UPDATE history SET isFavorite = :favorite WHERE id = :id")
    suspend fun setFavorite(id: Long, favorite: Boolean)

    @Delete
    suspend fun delete(entity: HistoryEntity)

    @Query("DELETE FROM history")
    suspend fun clearAll()

    @Query("SELECT COUNT(*) FROM history")
    suspend fun count(): Int
}
