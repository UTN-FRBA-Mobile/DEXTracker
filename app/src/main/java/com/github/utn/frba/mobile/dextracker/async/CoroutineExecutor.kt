package com.github.utn.frba.mobile.dextracker.async

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object AsyncCoroutineExecutor {
    fun dispatch(f: suspend CoroutineScope.() -> Unit) = GlobalScope.launch(
        context = Dispatchers.IO,
        block = f
    )
}
