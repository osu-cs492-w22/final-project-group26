package com.example.android.investaxchange.data

import android.content.Context
import androidx.room.RoomDatabase
import androidx.room.Room
import androidx.room.Database

const val DATABASE_NAME = "investa-xchange-db"

@Database(entities = [Asset::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun assetDao(): AssetDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .allowMainThreadQueries() // This is cheese, but idgaf
                .build()
    }
}