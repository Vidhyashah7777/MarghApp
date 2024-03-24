package com.example.marghappclonepriyankaparmar.roomDatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SavedImageDao {
    @Query("SELECT * FROM saved_images")
    fun getAllSavedImages(): LiveData<List<SavedImageEntity>>

    @Insert
    suspend fun insertSavedImage(savedImage: SavedImageEntity)

    @Query("Delete from saved_images where imageUrl = :imageURl")
    suspend fun deleteSavedImage(imageURl :String)

}