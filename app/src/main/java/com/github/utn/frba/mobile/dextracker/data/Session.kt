package com.github.utn.frba.mobile.dextracker.data

data class Session(
    val userId: String,
    val dexToken: String,
    val pokedex: List<PokedexRef>
) {
    constructor(token: String, user: User) : this(
        userId = user.userId,
        dexToken = token,
        pokedex = user.pokedex.map { PokedexRef(it) }
    )
}

data class PokedexRef(
    val id: String,
    val caught: Int,
    val total: Int,
    val game: Game,
) {
    constructor(pokedex: UserDex) : this(
        id = pokedex.userDexId,
        game = pokedex.game,
        caught = pokedex.caught,
        total = pokedex.pokemon.size,
    )
}

data class LoginRequest(
    val mail: String,
    val googleToken: String,
)
