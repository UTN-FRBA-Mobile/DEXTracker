package com.github.utn.frba.mobile.dextracker.data

data class User(
    val userId: String,
    val username: String? = null,
    val pokedex: List<UserDex>,
    val mail: String,
    val picture: String? = null,
)

data class UserDex(
    val userDexId: String,
    val game: Game,
    val type: PokedexType,
    val region: String,
    val name: String? = null,
    val pokemon: List<UserDexPokemon>,
    val caught: Int = 0,
)

data class UserDexPokemon(
    val name: String,
    val dexNumber: Int,
    val caught: Boolean,
)

data class UpdateUserDTO(
    val username: String? = null,
    val dex: Map<String, DexUpdateDTO>? = null,
)

data class DexUpdateDTO(
    val name: String? = null,
    val caught: List<Int>
)
