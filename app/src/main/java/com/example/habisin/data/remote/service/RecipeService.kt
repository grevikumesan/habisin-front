package com.example.habisin.data.remote.service

import com.example.habisin.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface RecipeService {

    @GET("resep/all")
    suspend fun getAllResep(): Response<ResepListResponse>

    @GET("resep/{id}")
    suspend fun getResepById(
        @Path("id") id: Int
    ): Response<ResepDetailResponse>

    @POST("resep/generate")
    suspend fun generateResep(
        @Body request: ResepGenerateRequest
    ): Response<ResepGenerateResponse>

    @DELETE("resep/remove/{id}")
    suspend fun deleteResep(
        @Path("id") id: Int
    ): Response<ResepDeleteResponse>
}