package com.github.utn.frba.mobile.dextracker.db.storage

import android.content.Context
import com.github.utn.frba.mobile.dextracker.data.Favourite
import com.github.utn.frba.mobile.dextracker.db.db
import com.github.utn.frba.mobile.dextracker.db.table.FavouritesTable
import com.github.utn.frba.mobile.dextracker.db.table.PokedexRow
import com.github.utn.frba.mobile.dextracker.db.table.SessionRow
import com.github.utn.frba.mobile.dextracker.model.PokedexRef
import com.github.utn.frba.mobile.dextracker.model.Session

class SessionStorage(context: Context) {
    private val sessionDao = db(context).sessionDao()
    private val pokedexDao = db(context).pokedexDao()
    private val favouritesDao = db(context).favouritesDao()

    suspend fun get(): Session? = sessionDao.storedSession()?.let { session ->
        val pokedex = pokedexDao.findAll(session.userId)
        val favourites = favouritesDao.favourites(session.userId)

        Session(
            userId = session.userId,
            dexToken = session.dexToken,
            pokedex = pokedex.map {
                PokedexRef(
                    id = it.id,
                    caught = it.caught,
                    total = it.total,
                    game = it.game,
                    name = it.dexName,
                )
            },
            favourites = favourites.map {
                Favourite(
                    species = it.species,
                    dexId = it.dexId,
                    gen = it.gen,
                )
            }
        )
    }

    suspend fun store(session: Session) {
        favouritesDao.drop()
        pokedexDao.drop()
        sessionDao.drop()

        sessionDao.save(
            SessionRow(
                dexToken = session.dexToken,
                userId = session.userId,
            )
        )
        pokedexDao.saveAll(session.pokedex.map {
            PokedexRow(
                id = it.id,
                caught = it.caught,
                total = it.total,
                game = it.game,
                userOwnerId = session.userId,
            )
        })
        favouritesDao.saveAll(session.favourites.map {
            FavouritesTable(
                gen = it.gen,
                dexId = it.dexId,
                species = it.species,
                ownerUserId = session.userId,
            )
        })
    }
}