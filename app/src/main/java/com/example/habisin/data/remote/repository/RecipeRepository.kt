package com.example.habisin.data.remote.repository

import com.example.habisin.data.remote.dto.*
import com.example.habisin.data.remote.service.RecipeService
import retrofit2.Response

class RecipeRepository(
    private val service: RecipeService
) {
    suspend fun getCatalog(
        page: Int = 1,
        limit: Int = 50,
        category: String? = null,
        search: String? = null
    ): Response<CatalogListResponse> =
        service.getCatalog(page, limit, category, search)

    suspend fun getCatalogById(id: Int): Response<CatalogDetailResponse> =
        service.getCatalogById(id)

    suspend fun getCatalogCategories(): Response<CatalogCategoriesResponse> =
        service.getCatalogCategories()

    suspend fun getAllResep(): Response<ResepListResponse> =
        service.getAllResep()

    suspend fun getResepById(id: Int): Response<ResepDetailResponse> =
        service.getResepById(id)

    suspend fun generateResep(saveToHistory: Boolean = false): Response<ResepGenerateResponse> =
        service.generateResep(ResepGenerateRequest(saveToHistory))

    suspend fun deleteResep(id: Int): Response<ResepDeleteResponse> =
        service.deleteResep(id)
}