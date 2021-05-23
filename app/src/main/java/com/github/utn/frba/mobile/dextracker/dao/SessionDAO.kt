package com.github.utn.frba.mobile.dextracker.dao

import androidx.room.Query
import com.github.utn.frba.mobile.dextracker.model.Session

interface SessionDAO {
    @Query("SELECT * from Session limit 1")
    fun storedSession(): Session?
}