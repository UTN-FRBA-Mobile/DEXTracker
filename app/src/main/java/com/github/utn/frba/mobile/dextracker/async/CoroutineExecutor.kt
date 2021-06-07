package com.github.utn.frba.mobile.dextracker.async

import kotlinx.coroutines.*

object AsyncCoroutineExecutor {
    fun dispatch(f: suspend CoroutineScope.() -> Unit) = GlobalScope.launch(
        context = Dispatchers.IO,
        block = f
    )

    fun <T> parallel(vararg blocks: suspend CoroutineScope.() -> T): List<T> = runBlocking {
        blocks.map { async(Dispatchers.IO) { it.invoke(this) } }.awaitAll()
    }
}
