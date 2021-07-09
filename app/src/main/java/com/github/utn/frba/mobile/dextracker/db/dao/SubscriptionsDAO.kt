package com.github.utn.frba.mobile.dextracker.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.github.utn.frba.mobile.dextracker.db.table.SubscriptionTable

@Dao
interface SubscriptionsDAO {
    @Query("select * from SubscriptionTable where ownerUserId = :userId")
    suspend fun subscriptions(userId: String): List<SubscriptionTable>

    @Insert
    suspend fun saveAll(pokedex: List<SubscriptionTable>)

    @Query("delete from SubscriptionTable")
    suspend fun drop()
}