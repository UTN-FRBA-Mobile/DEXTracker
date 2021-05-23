package com.github.utn.frba.mobile.dextracker.extensions

fun <A, B, T> T.both(fa: (T) -> A?, fb: (T) -> B?): Pair<A, B>? {
    val a = fa(this)
    val b = fb(this)

    return if (a != null && b != null) a to b else null
}
