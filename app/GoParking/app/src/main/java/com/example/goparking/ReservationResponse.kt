package com.example.goparking

data class ReservationResponse(
    val reservations: List<ReservationItem>
)

data class ReservationItem(
    val id: String,
    val user_id: String,
    val spot_id: String,
    val date: String,
    val duration: String,
    val hours: String,
    val total_cost: String
)
