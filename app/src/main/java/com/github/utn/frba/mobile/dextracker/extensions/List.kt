package com.github.utn.frba.mobile.dextracker.extensions

fun <T> List<T>.mapIf(cond: (T) -> Boolean, f: (T) -> T): List<T> = map {
    if (cond(it)) f(it) else it
}
