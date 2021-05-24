package com.github.utn.frba.mobile.dextracker.db.table

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.utn.frba.mobile.dextracker.data.Game

@Entity
data class PokedexRow(
    @PrimaryKey val id: String,
    val userOwnerId: String,
    val caught: Int,
    val total: Int,
    @Embedded val game: Game,
)