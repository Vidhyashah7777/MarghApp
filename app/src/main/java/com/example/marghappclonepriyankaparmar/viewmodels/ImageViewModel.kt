package com.example.retrofitmvvm.retrofitAll.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marghappclonepriyankaparmar.apicall.models.ImageResponse
import com.example.marghappclonepriyankaparmar.roomDatabase.SavedImageEntity
import com.example.retrofitmvvm.retrofitAll.repository.ImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class ImageViewModel(private val repository: ImageRepository) : ViewModel() {

    fun getImages(selectedCategory: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getImages(selectedCategory)
        }
    }

    val images: LiveData<ImageResponse>
        get() = repository.images

    val allData: LiveData<List<SavedImageEntity>> = repository.allData
}