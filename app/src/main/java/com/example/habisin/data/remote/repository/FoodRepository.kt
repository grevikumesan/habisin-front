package com.example.habisin.data.remote.repository

import com.example.habisin.data.remote.dto.AddFoodRequest
import com.example.habisin.data.remote.service.FoodService
import com.example.habisin.ui.model.ProductModel
import com.example.habisin.util.SessionManager

class FoodRepository(
    private val service: FoodService,
    private val sessionManager: SessionManager
) {

    suspend fun create(request: AddFoodRequest): Result<ProductModel> {
        return try {
            val token = sessionManager.getToken()
                ?: return Result.failure(IllegalStateException("Not logged in"))

            val response = service.create("Bearer $token", request)
            if (response.isSuccessful) {
                response.body()?.data?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAll(): Result<List<ProductModel>> {
        return try {
            val token = sessionManager.getToken()
                ?: return Result.failure(IllegalStateException("Not logged in"))

            val response = service.getAll("Bearer $token")
            if (response.isSuccessful) {
                response.body()?.data?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}