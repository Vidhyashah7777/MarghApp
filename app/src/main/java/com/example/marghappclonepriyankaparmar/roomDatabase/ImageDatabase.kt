package com.example.marghappclonepriyankaparmar.roomDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SavedImageEntity::class], version = 1)
abstract class ImageDatabase : RoomDatabase() {
    abstract fun savedImageDao(): SavedImageDao
    companion object {
        @Volatile
        private var instance: ImageDatabase? = null
        fun getDatabase(context: Context): ImageDatabase {
            return instance ?: synchronized(this) {
                val newInstance = Room.databaseBuilder(
                    context.applicationContext,
                    ImageDatabase::class.java,
                    "image_database"
                ).build()
                instance = newInstance
                newInstance
            }
        }
    }
}