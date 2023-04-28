package com.rickyputrah.search.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ClientModule {

    @[Provides Singleton]
    internal fun providesSearchClient(gson: Gson, okHttpClient: OkHttpClient): SearchClient {
        val retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://duckduckgo.com")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        return retrofit.create(SearchClient::class.java)
    }

    @[Provides Singleton]
    internal fun providesOkHttp(): OkHttpClient = OkHttpClient().newBuilder().build()

    @[Provides Singleton]
    internal fun provideGson(): Gson = GsonBuilder().setLenient().create()
}