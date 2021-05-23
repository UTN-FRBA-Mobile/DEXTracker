package com.github.utn.frba.mobile.dextracker.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.github.utn.frba.mobile.dextracker.dao.SessionDAO
import com.github.utn.frba.mobile.dextracker.model.PokedexRef
import com.github.utn.frba.mobile.dextracker.model.Session
import com.github.utn.frba.mobile.dextracker.utils.Memoized
import com.github.utn.frba.mobile.dextracker.utils.memo

val db: Memoized<Context, AppDatabase> = memo {
    Room.databaseBuilder(
        it,
        AppDatabase::class.java,
        "dex-tracker"
    ).build()
}

@Database(entities = [Session::class, PokedexRef::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sessionDao(): SessionDAO
}