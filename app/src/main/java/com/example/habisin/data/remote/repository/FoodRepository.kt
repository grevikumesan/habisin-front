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
                val body = response.body()
                if (body != null) {
                    Result.success(body.data)
                } else {
                    Result.failure(Exception("Empty response body"))
                }
            } else {
                Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}