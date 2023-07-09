package com.example.goparking

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BookingInfo(
    val id: String,
    val parkingSpot: String,
    val date: String,
    val duration: String,
    val hours: String,
    val totalCost: String
) : Serializable
