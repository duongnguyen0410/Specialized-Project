package com.example.goparking

data class RoutesApiResponse(
    val routes: List<Route>
)

data class Route(
    val duration: String,
    val distanceMeters: Long,
    val polyline: Polyline
)

data class Polyline(
    val encodedPolyline: String
)
