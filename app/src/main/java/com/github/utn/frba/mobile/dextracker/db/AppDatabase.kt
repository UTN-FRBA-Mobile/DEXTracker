package com.github.utn.frba.mobile.dextracker.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.github.utn.frba.mobile.dextracker.db.dao.FavouritesDAO
import com.github.utn.frba.mobile.dextracker.db.dao.PokedexDAO
import com.github.utn.frba.mobile.dextracker.db.dao.SessionDAO
import com.github.utn.frba.mobile.dextracker.db.dao.SubscriptionsDAO
import com.github.utn.frba.mobile.dextracker.db.table.FavouritesTable
import com.github.utn.frba.mobile.dextracker.db.table.PokedexTable
import com.github.utn.frba.mobile.dextracker.db.table.SessionTable
import com.github.utn.frba.mobile.dextracker.db.table.SubscriptionTable
import com.github.utn.frba.mobile.dextracker.utils.Memoized
import com.github.utn.frba.mobile.dextracker.utils.memo

val db: Memoized<Context, AppDatabase> = memo {
    Room.databaseBuilder(
        it,
        AppDatabase::class.java,
        "dex-tracker"
    )
        .fallbackToDestructiveMigration()
        .build()
}

@Database(
    entities = [
        SessionTable::class,
        PokedexTable::class,
        FavouritesTable::class,
        SubscriptionTable::class
    ],
    version = 3,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sessionDao(): SessionDAO

    abstract fun pokedexDao(): PokedexDAO

    abstract fun favouritesDao(): FavouritesDAO

    abstract fun subscriptionsDao(): SubscriptionsDAO
}