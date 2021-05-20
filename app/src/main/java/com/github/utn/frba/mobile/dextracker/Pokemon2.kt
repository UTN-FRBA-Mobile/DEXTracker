package com.github.utn.frba.mobile.dextracker

data class Pokemon2(
    var name: String,
    var nationalPokedexNumber: Int,
    var primaryAbility: String,
    var secondaryAbility: String?,
    var hiddenAbility: String?,
    var evolutions: List<Evolution2>,
    var forms: List<Form2>,
    var gen: Int,
)
data class Evolution2(
    var name: String,
    var method: EvolutionMethod2,
)
data class EvolutionMethod2(
    var type: String,
    var level: Int?,
    var friendship: Int?,
    var move: String?,
    var location: String?,
    var time: String?,
    var item: String?,
    var gender: String?,
    var upsideDown: Boolean?,
    var region: String?,
    var pokemon: String?,
)
data class Form2(
    var name: String,
)