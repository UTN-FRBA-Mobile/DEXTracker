package com.github.utn.frba.mobile.dextracker.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.github.utn.frba.mobile.dextracker.db.table.FavouritesTable

@Dao
interface FavouritesDAO {
    @Query("select * from FavouritesTable where ownerUserId = :userId")
    suspend fun favourites(userId: String): List<FavouritesTable>

    @Insert
    suspend fun saveAll(pokedex: List<FavouritesTable>)

    @Query("delete from FavouritesTable")
    suspend fun drop()
}