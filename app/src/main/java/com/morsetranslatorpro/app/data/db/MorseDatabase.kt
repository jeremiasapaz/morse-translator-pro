package com.morsetranslatorpro.app.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [HistoryEntity::class], version = 1, exportSchema = true)
abstract class MorseDatabase : RoomDatabase() {

    abstract fun historyDao(): HistoryDao

    companion object {
        private const val DB_NAME = "morse_history.db"

        @Volatile
        private var instance: MorseDatabase? = null

        fun get(context: Context): MorseDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    MorseDatabase::class.java,
                    DB_NAME
                ).fallbackToDestructiveMigration().build().also { instance = it }
            }
    }
}
