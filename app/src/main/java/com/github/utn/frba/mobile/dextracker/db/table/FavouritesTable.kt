package com.github.utn.frba.mobile.dextracker.db.table

import androidx.room.Entity

@Entity(primaryKeys = ["dexId", "species"])
data class FavouritesTable(
    val ownerUserId: String,
    val dexId: String,
    val species: String,
    val gen: Int,
)
