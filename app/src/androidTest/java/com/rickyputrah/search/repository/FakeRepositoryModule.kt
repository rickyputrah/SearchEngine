package com.rickyputrah.search.repository

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

val searchRepository = FakeSearchRepository()

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class],
)
class FakeRepositoryModule {

    @Provides
    fun provideSearchRepository(): SearchRepository = searchRepository
}

