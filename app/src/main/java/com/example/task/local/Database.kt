package com.example.task.local

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

@androidx.room.Database(
    entities = [Data::class],
    version = 1
)
abstract class Database : RoomDatabase() {

    abstract fun dataDao() : DataDao

    companion object {
        @Volatile
        private var instance: Database? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                Database::class.java,
                "database.db"
            ).build()
    }
}