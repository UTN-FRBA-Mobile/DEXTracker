package com.github.utn.frba.mobile.dextracker

data class Evolution(
    val name: String,
    val method: EvolutionMethod,
)

sealed class EvolutionMethod(val type: String)

data class LevelUp(
    val level: Int? = null,
    val friendship: Int? = null,
    val move: String? = null,
    val location: String? = null,
    val time: String? = null,
    val item: String? = null,
    val gender: String? = null,
    val upsideDown: Boolean? = null,
    val region: String? = null,
) : EvolutionMethod(type = "LEVEL_UP")

data class UseItem(
    val item: String,
    val gender: String? = null,
    val region: String? = null,
) : EvolutionMethod(type = "USE_ITEM")

data class Trade(
    val item: String? = null,
    val pokemon: String? = null,
) : EvolutionMethod(type = "TRADE")