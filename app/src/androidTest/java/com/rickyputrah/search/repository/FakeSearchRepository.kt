package com.rickyputrah.search.repository

import com.rickyputrah.search.network.SearchResponse

class FakeSearchRepository : SearchRepository {

    var result = Result.success(searchList)

    override suspend fun getRecommendation(query: String): Result<List<SearchResponse>> {
        return result
    }

    fun reset() {
        result = Result.success(searchList)
    }

    companion object {
        private val searchList = listOf(SearchResponse("google"), SearchResponse("bing"), SearchResponse("duckduckgo"))
    }
}