package com.github.utn.frba.mobile.dextracker.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.github.utn.frba.mobile.dextracker.db.table.SessionRow

@Dao
interface SessionDAO {
    @Transaction
    @Query("SELECT * from SessionRow limit 1")
    suspend fun storedSession(): SessionRow?

    @Insert
    suspend fun save(row: SessionRow)

    @Query("DELETE FROM SessionRow")
    suspend fun drop()
}