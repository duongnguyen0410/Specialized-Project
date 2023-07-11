package com.example.goparking

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ParkingApiService {

    @POST("/user/create")
    fun createUser(
        @Body requestBody: UserApiBody
    ): Call<List<UserApiResponse>>

    @POST("/user/login")
    fun loginUser(
        @Query("email") email: String,
        @Query("password") password: String,
    ): Call<UserApiResponse>

    @GET("/spot/get/{id}")
    fun getSpot(
        @Path("id") spotId: String
    ): Call<SpotApiResponse>

    @PUT("/spot/update/{id}")
    fun putSpot(
        @Path("id") spotId: String,
        @Query("state") spotStatus: String
    ): Call<SpotApiResponse>

    @GET("/reservation/get/{id}")
    fun getReservation(
        @Path("id") reservationId: String
    ): Call<ReservationResponse>

    @POST("/reservation/create")
    fun createReservation(
        @Body requestBody: ReservationApiBody
    ): Call<ReservationResponse>
}