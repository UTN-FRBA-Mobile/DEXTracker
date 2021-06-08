package com.github.utn.frba.mobile.dextracker.extensions

infix fun Int.percentageOf(that: Int): Int = ((this.toFloat() / that.toFloat()) * 100).toInt()
