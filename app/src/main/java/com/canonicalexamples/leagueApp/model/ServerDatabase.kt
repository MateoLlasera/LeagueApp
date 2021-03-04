package com.canonicalexamples.leagueApp.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Server::class], version = 1, exportSchema = false)
abstract class ServerDatabase: RoomDatabase() {
    abstract val serverDao: ServerDao

    companion object {
        @Volatile
        private var INSTANCE: ServerDatabase? = null
        fun getInstance(context: Context): ServerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ServerDatabase::class.java,
                    "server_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}