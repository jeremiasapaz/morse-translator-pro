package com.morsetranslatorpro.app

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.morsetranslatorpro.app.data.db.HistoryDao
import com.morsetranslatorpro.app.data.db.HistoryEntity
import com.morsetranslatorpro.app.data.db.MorseDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HistoryDaoTest {

    private lateinit var db: MorseDatabase
    private lateinit var dao: HistoryDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        db = Room.inMemoryDatabaseBuilder(context, MorseDatabase::class.java).build()
        dao = db.historyDao()
    }

    @After
    fun teardown() = db.close()

    private fun entity(text: String, fav: Boolean = false) = HistoryEntity(
        timestamp = System.currentTimeMillis(),
        originalText = text,
        morse = "...",
        direction = "ENCODE",
        wpm = 15,
        toneFrequencyHz = 700,
        isFavorite = fav
    )

    @Test
    fun insertAndObserve() = runBlocking {
        dao.insert(entity("HELLO"))
        val all = dao.observeAll().first()
        assertEquals(1, all.size)
        assertEquals("HELLO", all.first().originalText)
    }

    @Test
    fun favoriteFilter() = runBlocking {
        val id = dao.insert(entity("ONE"))
        dao.insert(entity("TWO"))
        dao.setFavorite(id, true)
        val favs = dao.observeFavorites().first()
        assertEquals(1, favs.size)
        assertTrue(favs.first().isFavorite)
    }

    @Test
    fun search() = runBlocking {
        dao.insert(entity("HELLO WORLD"))
        dao.insert(entity("GOODBYE"))
        val results = dao.search("WORLD").first()
        assertEquals(1, results.size)
    }

    @Test
    fun clearAll() = runBlocking {
        dao.insert(entity("A"))
        dao.insert(entity("B"))
        dao.clearAll()
        assertEquals(0, dao.count())
    }
}
