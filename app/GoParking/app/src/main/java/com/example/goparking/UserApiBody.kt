package com.example.goparking

data class UserApiBody(
    val email: String,
    val password: String,
    val name: String,
    val phone: String,
    val license_plate: String
)
