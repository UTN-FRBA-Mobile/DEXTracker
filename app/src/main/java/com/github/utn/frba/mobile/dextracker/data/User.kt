package com.github.utn.frba.mobile.dextracker.data

data class User(
    val userId: String,
    val username: String? = null,
    val pokedex: List<UserDex>,
    val mail: String,
    val picture: String? = null,
    val favourites: List<Favourite>,
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
    val favourites: List<Favourite>,
)

data class DexUpdateDTO(
    val name: String? = null,
    val caught: List<Int>,
)

data class Favourite(
    val species: String,
    val gen: Int,
    val dexId: String,
)

data class DexRequest(
    val game: String,
    val name: String? = null,
)