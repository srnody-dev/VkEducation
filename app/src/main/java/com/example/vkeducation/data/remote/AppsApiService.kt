package com.example.vkeducation.data.remote

import retrofit2.http.GET
import retrofit2.http.Path

interface AppsApiService {
    @GET("catalog")
    suspend fun loadApps(): List<AppDto>

    @GET("catalog/{id}")
    suspend fun getAppById(@Path("id") id: String): AppDto
}