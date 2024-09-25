package com.example.locationtrackerapp.model

data class CustomPlace(
    val placeId: String,
    val name: String,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)
