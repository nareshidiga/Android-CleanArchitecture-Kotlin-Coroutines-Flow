package com.cleanarch.features.wikientry.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cleanarch.app.AppDatabase
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WikiEntryDaoTest {

    @get:Rule var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: AppDatabase

    @Before fun initDb() {
        database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After fun closeDb() {
        database.close()
    }

    @Test fun testInsertAndGetWikiEntry() = runBlocking {
        database.wikiEntryDao().insert(WIKI_ENTRY)
        val list = database.wikiEntryDao()
            .getByTitle(WIKI_ENTRY.title)
            .firstOrNull()
        val item = list?.get(0)
        assertEquals(WIKI_ENTRY.pageId, item?.pageId)
        assertEquals(WIKI_ENTRY.title, item?.title)
        assertEquals(WIKI_ENTRY.extract, item?.extract)
   }

    @Test fun testGetWikiEntryWhenNoneInserted() = runBlocking {
        val list = database.wikiEntryDao()
            .getByTitle(WIKI_ENTRY.title)
            .firstOrNull()
        assertTrue(list!!.isEmpty())
    }

    @Test fun testDeleteAndGetWikiEntry() = runBlocking {
        database.wikiEntryDao().insert(WIKI_ENTRY)
        database.wikiEntryDao().delete(WIKI_ENTRY)
        val list = database.wikiEntryDao()
            .getByTitle(WIKI_ENTRY.title)
            .firstOrNull()
        assertTrue(list!!.isEmpty())
    }

    companion object {
        private val WIKI_ENTRY = WikiEntryTable(pageId = 17867,
            title = "London",
            extract = "The City of London, its ancient core and financial centre, " +
                    "was founded by the Romans as Londinium "
        )
    }
}