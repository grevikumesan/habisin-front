package com.example.habisin.data.remote.service

import com.example.habisin.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface RecipeService {

    // Browse catalog (the 16 seeded recipes). Auth handled by the OkHttp interceptor.
    @GET("catalog")
    suspend fun getCatalog(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 50,
        @Query("category") category: String? = null,
        @Query("search") search: String? = null
    ): Response<CatalogListResponse>

    @GET("catalog/{id}")
    suspend fun getCatalogById(
        @Path("id") id: Int
    ): Response<CatalogDetailResponse>

    @GET("catalog/categories")
    suspend fun getCatalogCategories(): Response<CatalogCategoriesResponse>

    // User's SAVED recipes (empty for new accounts).
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