package com.example.goparking

data class ReservationApiBody(
    val user_id: String,
    val spot_id: String,
    val date: String,
    val duration: String,
    val hours: String,
    val total_cost: String
)
