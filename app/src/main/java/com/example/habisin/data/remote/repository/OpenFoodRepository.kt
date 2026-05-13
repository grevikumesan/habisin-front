package com.example.habisin.data.remote.repository

import com.example.habisin.data.remote.service.OpenFoodService

class OpenFoodRepository(
    private val service: OpenFoodService
) {
    suspend fun getProduct(barcode: String): Result<String> {
        return try {
            val response = service.getProductByBarcode(barcode)

            if(response.isSuccessful) {
                val product = response.body()?.product

                val name = product?.product_name ?: "No Name"

                Result.success(
                    name
                )
            } else {
                Result.failure(
                    Exception("Failed: ${response.code()}")
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}