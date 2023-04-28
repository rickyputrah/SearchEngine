package com.rickyputrah.search.repository

import com.rickyputrah.search.network.SearchClient
import com.rickyputrah.search.network.SearchResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SearchRepositoryTest {

    private val searchClient = mockk<SearchClient>()

    @Test
    fun `Given get recommendation success When search repo get recommendation Then return the list of recommendation`() = runTest {
        coEvery { searchClient.getRecommendation("query") } returns searchList

        val result = searchRepository.getRecommendation("query")

        assertEquals(searchList, result.getOrThrow())
    }

    @Test
    fun `Given get recommendation throw exception When search repo get recommendation Then return failure`() = runTest {
        coEvery { searchClient.getRecommendation("query") } throws Throwable("")

        val result = searchRepository.getRecommendation("")

        assertTrue(result.isFailure)
    }

    companion object {
        private val searchList = listOf(SearchResponse("google"), SearchResponse("bing"), SearchResponse("duckduckgo"))
    }

    private val searchRepository by lazy(LazyThreadSafetyMode.NONE) {
        DefaultSearchRepository(
            searchClient = searchClient
        )
    }
}