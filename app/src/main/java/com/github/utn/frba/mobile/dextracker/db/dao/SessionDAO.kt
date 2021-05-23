package com.github.utn.frba.mobile.dextracker.db.dao

import androidx.room.*
import com.github.utn.frba.mobile.dextracker.db.table.SessionRow

@Dao
interface SessionDAO {
    @Transaction
    @Query("SELECT * from SessionRow limit 1")
    fun storedSession(): SessionRow?

    @Insert
    fun save(row: SessionRow)

    @Delete
    fun delete(row: SessionRow)
}