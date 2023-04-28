package com.rickyputrah.search.dispatcher

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

interface AppDispatchers {
    val Main: CoroutineDispatcher
    val IO: CoroutineDispatcher
    val Default: CoroutineDispatcher
    val Unconfined: CoroutineDispatcher
}

@Singleton
class AppDispatchersImpl constructor(
    override val Main: CoroutineDispatcher = Dispatchers.Main,
    override val IO: CoroutineDispatcher = Dispatchers.IO,
    override val Default: CoroutineDispatcher = Dispatchers.Default,
    override val Unconfined: CoroutineDispatcher = Dispatchers.Unconfined
) : AppDispatchers {
    constructor(dispatcher: CoroutineDispatcher) : this(dispatcher, dispatcher, dispatcher, dispatcher)
}
