package com.example.retrofitmvvm.retrofitAll.retrofit


import com.example.marghappclonepriyankaparmar.apicall.models.ImageResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("api/")
    suspend fun getImages(
        @Query("key") apiKey: String,
        @Query("category") category: String,
        @Query("orientation") orientation: String
    ): Response<ImageResponse>
}