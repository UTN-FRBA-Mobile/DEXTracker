package com.github.utn.frba.mobile.dextracker.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.github.utn.frba.mobile.dextracker.db.table.PokedexTable

@Dao
interface PokedexDAO {
    @Query("SELECT * from PokedexTable where userOwnerId = :userId")
    suspend fun findAll(userId: String): List<PokedexTable>

    @Query("DELETE from PokedexTable where userOwnerId = :userId")
    suspend fun delete(userId: String)

    @Insert
    suspend fun saveAll(pokedex: List<PokedexTable>)

    @Query("delete from PokedexTable")
    suspend fun drop()
}