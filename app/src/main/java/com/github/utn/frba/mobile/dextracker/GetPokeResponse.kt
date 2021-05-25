package com.github.utn.frba.mobile.dextracker

class GetPokeResponse {
    //lateinit var poke: Pokemon2
    lateinit var name: String
    var nationalPokedexNumber: Int = 0
    lateinit var primaryAbility: String
    var secondaryAbility: String? = null
    var hiddenAbility: String? = null
    lateinit var evolutions: List<Evolution4>
    lateinit var forms: List<Form4>
    var gen: Int = 0
}
class Evolution4{
    lateinit var name: String
    lateinit var method: EvolutionMethod2
}
class EvolutionMethod4{
    lateinit var type: String
    var level: Int? = null
    var friendship: Int? = null
    var move: String? = null
    var location: String? = null
    var time: String? = null
    var item: String? = null
    var gender: String? = null
    var upsideDown: Boolean? = null
    var region: String? = null
    var pokemon: String? = null
}
class Form4{
    lateinit var name: String
}