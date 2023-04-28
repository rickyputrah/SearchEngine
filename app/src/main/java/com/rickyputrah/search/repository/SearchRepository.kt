package com.rickyputrah.search.repository

import com.rickyputrah.search.network.SearchClient
import com.rickyputrah.search.network.SearchResponse
import javax.inject.Inject


interface SearchRepository {
    suspend fun getRecommendation(query: String): Result<List<SearchResponse>>
}

internal class DefaultSearchRepository @Inject constructor(
    private val searchClient: SearchClient
) : SearchRepository {

    override suspend fun getRecommendation(query: String): Result<List<SearchResponse>> {
        return runCatching { searchClient.getRecommendation(query) }
    }
}