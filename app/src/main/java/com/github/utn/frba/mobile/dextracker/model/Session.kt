package com.github.utn.frba.mobile.dextracker.model

import com.github.utn.frba.mobile.dextracker.data.*

data class Session(
    val dexToken: String,
    val userId: String,
    val pokedex: List<PokedexRef>,
    val favourites: List<Favourite>,
    val subscriptions: Set<Subscription>,
) {
    constructor(token: String, user: User) : this(
        userId = user.userId,
        dexToken = token,
        pokedex = user.pokedex.map { PokedexRef(it) },
        favourites = user.favourites,
        subscriptions = emptySet(),
    )
}

data class PokedexRef(
    val id: String,
    val caught: Int,
    val total: Int,
    val name: String?,
    val game: Game,
) {
    constructor(pokedex: UserDex) : this(
        id = pokedex.userDexId,
        game = pokedex.game,
        caught = pokedex.caught,
        total = pokedex.pokemon.size,
        name = pokedex.name,
    )
}
