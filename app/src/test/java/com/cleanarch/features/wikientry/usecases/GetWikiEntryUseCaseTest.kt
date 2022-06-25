package com.cleanarch.features.wikientry.usecases

import com.cleanarch.features.wikientry.data.WikiEntryRepo
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class GetWikiEntryUseCaseTest {

    @Mock
    private lateinit var mockRepo: WikiEntryRepo

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun testGetWikiEntryUseCase(): Unit = runBlocking{
        val useCase = GetWikiEntryUseCase(mockRepo)
        useCase.execute(GetWikiEntryUseCase.Input(WIKI_QUERY))
        verify(mockRepo).getWikiEntry(WIKI_QUERY)
    }

    companion object {
        private const val WIKI_QUERY = "TestTitle"
    }
}