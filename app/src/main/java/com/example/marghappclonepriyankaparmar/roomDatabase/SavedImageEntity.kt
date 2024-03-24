package com.example.marghappclonepriyankaparmar.roomDatabase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_images")
data class SavedImageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val imageUrl: String,
    var isLiked: Boolean = false
)
