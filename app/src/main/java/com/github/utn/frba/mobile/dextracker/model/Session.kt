package com.github.utn.frba.mobile.dextracker.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.utn.frba.mobile.dextracker.data.Game
import com.github.utn.frba.mobile.dextracker.data.User
import com.github.utn.frba.mobile.dextracker.data.UserDex

@Entity
data class Session(
    @ColumnInfo(name = "dex_token") val dexToken: String,
    @PrimaryKey @ColumnInfo(name = "user_id") val userId: String,
    @ColumnInfo(name = "pokedex") val pokedex: List<PokedexRef>
) {
    constructor(token: String, user: User) : this(
        userId = user.userId,
        dexToken = token,
        pokedex = user.pokedex.map { PokedexRef(it) }
    )
}

@Entity
data class PokedexRef(
    @PrimaryKey val id: String,
    val caught: Int,
    val total: Int,
    @Embedded val game: Game,
) {
    constructor(pokedex: UserDex) : this(
        id = pokedex.userDexId,
        game = pokedex.game,
        caught = pokedex.caught,
        total = pokedex.pokemon.size,
    )
}
