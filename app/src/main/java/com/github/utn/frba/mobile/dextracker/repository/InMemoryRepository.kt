package com.github.utn.frba.mobile.dextracker.repository

import com.github.utn.frba.mobile.dextracker.data.UserDex
import com.github.utn.frba.mobile.dextracker.extensions.mapIf
import com.github.utn.frba.mobile.dextracker.model.PokedexRef
import com.github.utn.frba.mobile.dextracker.model.Session

object InMemoryRepository {
    lateinit var session: Session
        @Synchronized get
        @Synchronized set

    fun merge(
        dexId: String,
        dex: UserDex
    ) {
        session = session.copy(
            pokedex = session.pokedex.mapIf({ it.id == dexId }) { PokedexRef(dex) },
        )
    }
}