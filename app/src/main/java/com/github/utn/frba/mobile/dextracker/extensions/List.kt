package com.github.utn.frba.mobile.dextracker.extensions

inline fun <T> List<T>.mapIf(cond: (T) -> Boolean, f: (T) -> T): List<T> = map {
    if (cond(it)) f(it) else it
}

inline fun <T, R> Iterable<T>.mapToSet(transform: (T) -> R): Set<R> = map(transform).toSet()
