package com.github.utn.frba.mobile.dextracker.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.github.utn.frba.mobile.dextracker.db.dao.PokedexDAO
import com.github.utn.frba.mobile.dextracker.db.dao.SessionDAO
import com.github.utn.frba.mobile.dextracker.db.table.PokedexRow
import com.github.utn.frba.mobile.dextracker.db.table.SessionRow
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

@Database(entities = [SessionRow::class, PokedexRow::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sessionDao(): SessionDAO

    abstract fun pokedexDao(): PokedexDAO
}