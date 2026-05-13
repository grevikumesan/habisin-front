package com.example.habisin.data.remote.service

import com.example.habisin.data.remote.dto.AddFoodRequest
import com.example.habisin.data.remote.dto.FoodListResponse
import com.example.habisin.data.remote.dto.FoodResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface FoodService {

    @GET("foods/all")
    suspend fun getAll(
        @Header("Authorization") token: String
    ): Response<FoodListResponse>

    @GET("foods/{id}")
    suspend fun getById(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<FoodResponse>

    @POST("foods/create")
    suspend fun create(
        @Header("Authorization") token: String,
        @Body request: AddFoodRequest
    ): Response<FoodResponse>

    @Multipart
    @POST("foods/create")
    suspend fun createWithImage(  // ← renamed
        @Header("Authorization") token: String,
        @Part("foodName") foodName: RequestBody,
        @Part("descriptionFood") descriptionFood: RequestBody,
        @Part("category") category: RequestBody,
        @Part("bestBefore") bestBefore: RequestBody,
        @Part("quantity") quantity: RequestBody,
        @Part image: MultipartBody.Part?
    ): Response<FoodResponse>

    @DELETE("foods/remove/{id}")
    suspend fun remove(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<FoodResponse>
}