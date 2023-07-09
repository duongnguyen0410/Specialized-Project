package com.example.goparking

import com.google.maps.internal.ApiResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Url

interface RoutesApiService {
    @Headers(
        "Content-Type: application/json",
        "X-Goog-FieldMask: routes.duration,routes.distanceMeters,routes.polyline.encodedPolyline"
    )
    @POST("directions/v2:computeRoutes")
    fun computeRoutes(
        @Header("X-Goog-Api-Key") apiKey: String,
        @Body requestBody: RoutesApiBody
    ): Call<RoutesApiResponse>
}