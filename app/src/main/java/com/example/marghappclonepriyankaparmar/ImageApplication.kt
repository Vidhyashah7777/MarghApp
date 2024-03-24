package com.example.marghappclonepriyankaparmar

import android.app.Application
import androidx.room.Room
import com.example.marghappclonepriyankaparmar.roomDatabase.ImageDatabase
import com.example.retrofitmvvm.retrofitAll.repository.ImageRepository
import com.example.retrofitmvvm.retrofitAll.retrofit.ApiInterface
import com.example.retrofitmvvm.retrofitAll.retrofit.RetrofitInstance

class ImageApplication : Application() {
    lateinit var imageRepositoiry: ImageRepository
    lateinit var database: ImageDatabase

    override fun onCreate() {
        super.onCreate()
        init()
    }

    private fun init() {
        val apiInterface = RetrofitInstance.getInstance().create(ApiInterface::class.java)
        database = Room.databaseBuilder(applicationContext, ImageDatabase::class.java, "image-database").build()
        imageRepositoiry = ImageRepository(apiInterface, applicationContext, database.savedImageDao())
    }
}