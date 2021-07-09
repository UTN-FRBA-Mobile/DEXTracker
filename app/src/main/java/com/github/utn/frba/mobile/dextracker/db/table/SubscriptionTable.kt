package com.github.utn.frba.mobile.dextracker.db.table

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SubscriptionTable(
    @PrimaryKey val id: String,
    val ownerUserId: String,
    val subscribedDexUserId: String,
    val subscribedDexId: String,
)
