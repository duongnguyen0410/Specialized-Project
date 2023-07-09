package com.example.goparking

data class RoutesApiBody(
    val origin: Origin,
    val destination: Destination,
    val travelMode: String,
    val routingPreference: String,
    val departureTime: String,
    val computeAlternativeRoutes: Boolean,
    val routeModifiers: RouteModifiers,
    val languageCode: String,
    val units: String
) {
    constructor(
        origin: Origin,
        destination: Destination,
        travelMode: String,
        routingPreference: String,
        departureTime: String,
        computeAlternativeRoutes: Boolean,
        routeModifiers: RouteModifiers,
        languageCode: String,
        units: String,
        avoidTolls: Boolean,
        avoidHighways: Boolean,
        avoidFerries: Boolean
    ) : this(
        origin = origin,
        destination = destination,
        travelMode = travelMode,
        routingPreference = routingPreference,
        departureTime = departureTime,
        computeAlternativeRoutes = computeAlternativeRoutes,
        routeModifiers = RouteModifiers(
            avoidTolls = avoidTolls,
            avoidHighways = avoidHighways,
            avoidFerries = avoidFerries
        ),
        languageCode = languageCode,
        units = units
    )
}

data class Origin(
    val location: Location
)

data class Destination(
    val location: Location
)

data class Location(
    val latLng: LatLng
)

data class LatLng(
    val latitude: Double,
    val longitude: Double
)

data class RouteModifiers(
    val avoidTolls: Boolean,
    val avoidHighways: Boolean,
    val avoidFerries: Boolean
)
