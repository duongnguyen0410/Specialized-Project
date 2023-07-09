package com.example.goparking

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RoutesApiClient {

    private const val BASE_URL = "https://routes.googleapis.com/"

    fun create(): RoutesApiService {
        val client = OkHttpClient.Builder().build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(RoutesApiService::class.java)
    }

}