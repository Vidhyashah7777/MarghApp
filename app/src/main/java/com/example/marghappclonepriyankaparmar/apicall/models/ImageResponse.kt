package com.example.marghappclonepriyankaparmar.apicall.models

data class ImageResponse(
    val hits: List<Hit>,
    val total: Int,
    val totalHits: Int
)