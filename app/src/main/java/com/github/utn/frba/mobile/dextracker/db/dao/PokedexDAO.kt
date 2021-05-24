package com.github.utn.frba.mobile.dextracker.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.github.utn.frba.mobile.dextracker.db.table.PokedexRow

@Dao
interface PokedexDAO {
    @Query("SELECT * from PokedexRow where userOwnerId = :userId")
    suspend fun findAll(userId: String): List<PokedexRow>

    @Query("DELETE from PokedexRow where userOwnerId = :userId")
    suspend fun delete(userId: String)

    @Insert
    suspend fun saveAll(pokedex: List<PokedexRow>)
}