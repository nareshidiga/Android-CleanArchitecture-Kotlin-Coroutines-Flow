package com.cleanarch.features.wikientry.data

import android.util.Log
import com.cleanarch.app.AppDatabase
import com.cleanarch.features.wikientry.data.local.WikiEntryTable
import com.cleanarch.features.wikientry.data.remote.WikiApiService
import com.cleanarch.features.wikientry.data.remote.WikiEntryApiResponse
import com.cleanarch.features.wikientry.entities.WikiEntry
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import java.io.InvalidObjectException
import javax.inject.Inject
import javax.inject.Singleton

/*
 * Copyright (C) 2017 Naresh Gowd Idiga
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@Singleton
class WikiEntryRepoImpl @Inject
constructor(private val appDatabase: AppDatabase, private val wikiApiService: WikiApiService) :
    WikiEntryRepo {

    // Solution-1: make local db as the single source of truth
    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getWikiEntry(title: String): Flow<WikiEntry> {
        Log.d(TAG, "fetch from local")
        val entries = appDatabase.wikiEntryDao().getByTitle(title)
        return entries.flatMapLatest { wikiEntryTables ->
            if (wikiEntryTables.isNotEmpty()) {
                val firstEntry = wikiEntryTables[0]
                Log.d(TAG, "sending entry from local")
                flowOf(
                    WikiEntry(
                        firstEntry.pageId,
                        firstEntry.title, firstEntry.extract
                    )
                )
            } else {
                Log.d(TAG, "no saved entry found in local")
                fetchFromRemote(title)
                flowOf(WikiEntry(0, "", ""))
            }
        }.filter { it.pageId > 0 }
    }

    // Solution-2: fetch either from local or from remote
    /*override suspend fun getWikiEntry(title: String): Flow<WikiEntry> {
        val local = fetchFromLocal(title)
        val remote = fetchFromRemote(title)
        return flowOf(local, remote).flattenConcat().filter { it.title.isNotEmpty() }
    }*/

    private suspend fun fetchFromRemote(title: String) {
        Log.d(TAG, "fetch from remote")
        val response = wikiApiService.getWikiEntry(title)
        Log.d(TAG, "received response from remote")
        val pageValIterator = response.query?.pages!!.values.iterator()
        val pageVal = pageValIterator.next()
        if (invalidResult(pageVal)) {
            Log.d(TAG, "received invalid result from remote")
            throw InvalidObjectException("Invalid Result")
        } else {
            val wikiEntry = WikiEntry(pageVal.pageid!!, pageVal.title!!, pageVal.extract!!)
            addNewEntryToLocalDB(wikiEntry)
        }
    }

    private fun addNewEntryToLocalDB(wikiEntry: WikiEntry) {
        appDatabase.runInTransaction {
            val newEntry = WikiEntryTable(
                wikiEntry.pageId,
                wikiEntry.title,
                wikiEntry.extract
            )
            val entryDao = appDatabase.wikiEntryDao()
            entryDao.insert(newEntry)
        }
        Log.d(TAG, "added new entry into app database table")
    }

    private fun invalidResult(pageVal: WikiEntryApiResponse.Pageval): Boolean {
        return pageVal.pageid == null || pageVal.pageid!! <= 0 ||
                pageVal.title == null || pageVal.title!!.isEmpty() ||
                pageVal.extract == null || pageVal.extract!!.isEmpty()
    }

    companion object {
        private val TAG: String = WikiEntryRepoImpl::class.java.simpleName
    }

}