package com.cleanarch.features.wikientry.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cleanarch.R
import com.cleanarch.app.CleanArchApp
import com.cleanarch.features.wikientry.entities.WikiEntry
import com.cleanarch.features.wikientry.usecases.GetWikiEntryUseCase
import dagger.Lazy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

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

class WikiEntryViewModel(application: Application) : AndroidViewModel(application) {

    private var wikiEntry: MutableLiveData<WikiEntry>

    @Inject
    lateinit var getWikiEntryUseCase: Lazy<GetWikiEntryUseCase>

    init {
        (application as CleanArchApp).wikiEntryComponent?.inject(this)
        wikiEntry = MutableLiveData<WikiEntry>()
    }

    internal fun getWikiEntry(): LiveData<WikiEntry> {
        return wikiEntry
    }

    internal fun loadWikiEntry(title: String) {
        viewModelScope.launch {
            val item = withContext(Dispatchers.IO) {
                getWikiEntryUseCase.get()
                    .execute(GetWikiEntryUseCase.Input(title))
                    .onCompletion { Log.d(TAG,"completed wiki query") }
                    .catch { exception -> Log.d(TAG, "Received $exception") }
                    .firstOrNull()
            }
            wikiEntry.value = item ?: WikiEntry(
                -1,
                "",
                getApplication<Application>().getString(R.string.no_results_found)
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "onCleared")
    }

    companion object {
        private val TAG = WikiEntryViewModel::class.java.simpleName
    }
}
