package com.example.goparking

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ParkingApiClient {

    private const val BASE_URL = "http://192.168.1.24:8000"

    fun create(): ParkingApiService {
        val client = OkHttpClient.Builder().build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ParkingApiService::class.java)
    }

}