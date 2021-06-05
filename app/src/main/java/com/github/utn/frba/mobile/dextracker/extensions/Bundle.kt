package com.github.utn.frba.mobile.dextracker.extensions

import android.os.Bundle

fun Bundle.putStrings(vararg strings: Pair<String, String?>) {
    strings.forEach { putString(it.first, it.second) }
}
