package com.github.utn.frba.mobile.dextracker.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.github.utn.frba.mobile.dextracker.db.table.SessionTable

@Dao
interface SessionDAO {
    @Transaction
    @Query("SELECT * from SessionTable limit 1")
    suspend fun storedSession(): SessionTable?

    @Insert
    suspend fun save(row: SessionTable)

    @Query("DELETE FROM SessionTable")
    suspend fun drop()
}