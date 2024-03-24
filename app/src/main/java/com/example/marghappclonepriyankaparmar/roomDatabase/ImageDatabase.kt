package com.example.marghappclonepriyankaparmar.roomDatabase

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import java.util.concurrent.Executors

@Database(entities = [SavedImageEntity::class], version = 1)
abstract class ImageDatabase : RoomDatabase() {
    abstract fun savedImageDao(): SavedImageDao

    companion object {
        @Volatile
        private var instance: ImageDatabase? = null
        fun getDatabase(context: Context): ImageDatabase {
            return instance ?: synchronized(this) {
                val newInstance = Room.databaseBuilder(
                    context.applicationContext, ImageDatabase::class.java, "image_database"
                ).setQueryCallback({ query, bindArgs ->
                    Log.d("Priyanka demo", "Query: $query, BindArgs: $bindArgs")
                }, Executors.newSingleThreadExecutor()).build()
                instance = newInstance
                newInstance
            }
        }
    }
}