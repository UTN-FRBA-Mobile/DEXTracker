package com.github.utn.frba.mobile.dextracker.utils

fun <I, O> memo(f: (I) -> O): Memoized<I, O> = Memoized(f)

class Memoized<I, O>(
    private val generator: (I) -> O
) {
    private var value: O? = null

    operator fun invoke(input: I): O =
        if (value != null) value!! else generator(input).also { value = it }
}