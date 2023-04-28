package com.rickyputrah.search.ui.search

import com.rickyputrah.search.dispatcher.AppDispatchers
import com.rickyputrah.search.network.SearchResponse
import com.rickyputrah.search.repository.SearchRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {
    private val searchRepository = mockk<SearchRepository>()

    @Test
    fun `When init Then return uiState with empty string and empty list`() = runTest {
        assertEquals(SearchUiState("", emptyList()), viewModel.uiState)
    }

    @Test
    fun `Given get recommendation success When text changed Then return list of result`() = runTest {
        coEvery { searchRepository.getRecommendation("new text") } returns Result.success(searchList)

        viewModel.onSearchTextChanged("new text")

        assertEquals(SearchUiState("new text", listOf("google", "bing", "duckduckgo")), viewModel.uiState)
    }

    @Test
    fun `Given get recommendation failed When text changed Then return empty list`() = runTest {
        coEvery { searchRepository.getRecommendation("new text") } returns Result.failure(Throwable(""))

        viewModel.onSearchTextChanged("new text")

        assertEquals(SearchUiState("new text", emptyList()), viewModel.uiState)
    }

    companion object {
        private val searchList = listOf(SearchResponse("google"), SearchResponse("bing"), SearchResponse("duckduckgo"))
    }

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        SearchViewModel(
            searchRepository = searchRepository,
            appDispatchers = appDispatchers(UnconfinedTestDispatcher())
        )
    }

    private fun appDispatchers(dispatcher: CoroutineDispatcher): AppDispatchers =
        object : AppDispatchers {
            override val Main: CoroutineDispatcher
                get() = dispatcher
            override val IO: CoroutineDispatcher
                get() = dispatcher
            override val Default: CoroutineDispatcher
                get() = dispatcher
            override val Unconfined: CoroutineDispatcher
                get() = dispatcher
        }
}