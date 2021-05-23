package com.github.utn.frba.mobile.dextracker.db.storage

import android.content.Context
import com.github.utn.frba.mobile.dextracker.db.dao.PokedexDAO
import com.github.utn.frba.mobile.dextracker.db.dao.SessionDAO
import com.github.utn.frba.mobile.dextracker.db.db
import com.github.utn.frba.mobile.dextracker.db.table.PokedexRow
import com.github.utn.frba.mobile.dextracker.model.PokedexRef
import com.github.utn.frba.mobile.dextracker.model.Session

class SessionStorage(context: Context) {
    private val sessionDao: SessionDAO
    private val pokedexDao: PokedexDAO

    init {
        sessionDao = db(context).sessionDao()
        pokedexDao = db(context).pokedexDao()
    }

    fun get(): Session? = sessionDao.storedSession()?.let { session ->
        val pokedex = pokedexDao.findAll(session.userId)
        Session(
            userId = session.userId,
            dexToken = session.dexToken,
            pokedex = pokedex.map {
                PokedexRef(
                    id = it.id,
                    caught = it.caught,
                    total = it.total,
                    game = it.game,
                )
            },
        )
    }

    fun store(session: Session) {
        pokedexDao.delete(session.userId)
        pokedexDao.saveAll(session.pokedex.map {
            PokedexRow(
                id = it.id,
                caught = it.caught,
                total = it.total,
                game = it.game,
                userOwnerId = session.userId,
            )
        })
    }
}