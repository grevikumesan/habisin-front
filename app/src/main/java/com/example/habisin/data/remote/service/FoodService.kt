package com.example.habisin.data.remote.service

import com.example.habisin.data.remote.dto.AddFoodRequest
import com.example.habisin.data.remote.dto.FoodResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface FoodService {

    @POST("foods/")
    suspend fun create(
        @Header("Authorization") token: String,
        @Body request: AddFoodRequest
    ): Response<FoodResponse>
}