package com.rickyputrah.search.network

import retrofit2.http.GET
import retrofit2.http.Query


interface SearchClient {

    @GET("/ac")
    suspend fun getRecommendation(
        @Query("q") query: String,
        @Query("type") type: String = "json"
    ): List<SearchResponse>
}