package com.example.retrofitmvvm.retrofitAll.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.marghappclonepriyankaparmar.apicall.models.ImageResponse
import com.example.marghappclonepriyankaparmar.roomDatabase.SavedImageDao
import com.example.marghappclonepriyankaparmar.roomDatabase.SavedImageEntity
import com.example.retrofitmvvm.retrofitAll.retrofit.ApiInterface
import com.example.retrofitmvvm.utils.NetworkConnection

class ImageRepository(
    private val apiInterface: ApiInterface,
    private val context: Context,
    private val imageDao: SavedImageDao
) {
    private val imagesLiveData = MutableLiveData<ImageResponse>()
    val images: LiveData<ImageResponse>
        get() = imagesLiveData

    suspend fun getImages(category: String) {
        if (NetworkConnection.isInternetAvailable(context)) {
            val result = apiInterface.getImages("42912450-1dff573f5af4dd3355b07fa2e", category, "horizontal")
            if (result != null && result.body() != null) {
                imagesLiveData.postValue(result.body())
            }
        } else {

        }

    }

    val allData: LiveData<List<SavedImageEntity>> = imageDao.getAllSavedImages()

}