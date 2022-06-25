package com.cleanarch.features.wikientry.usecases

import com.cleanarch.base.usecases.UseCase
import com.cleanarch.features.wikientry.data.WikiEntryRepo
import com.cleanarch.features.wikientry.entities.WikiEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
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
class GetWikiEntryUseCase @Inject
internal constructor(private val repo: WikiEntryRepo) :
    UseCase<GetWikiEntryUseCase.Input, Flow<WikiEntry>>() {

    override suspend fun execute(input: Input): Flow<WikiEntry> {
        return repo.getWikiEntry(input.title)
    }

    class Input(val title: String)
}