package com.example.habisin.data.remote.repository

import com.example.habisin.data.remote.service.OpenFoodService

/** Best-effort fields pulled from an Open Food Facts barcode lookup. */
data class ScannedProduct(
    val name: String,
    val imageUrl: String?,
    val category: String,        // PRODUCE / DAIRY / MEAT / OTHER
    val description: String
)

class OpenFoodRepository(
    private val service: OpenFoodService
) {
    suspend fun getProduct(barcode: String): Result<ScannedProduct> {
        return try {
            val response = service.getProductByBarcode(barcode)

            if (response.isSuccessful) {
                val product = response.body()?.product
                    ?: return Result.failure(Exception("Product not found"))

                Result.success(
                    ScannedProduct(
                        name        = product.product_name?.takeIf { it.isNotBlank() } ?: "",
                        imageUrl    = product.image_front_url ?: product.image_url,
                        category    = mapCategory(product.categories_tags),
                        description = product.generic_name ?: product.brands ?: ""
                    )
                )
            } else {
                Result.failure(Exception("Failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** Map Open Food Facts category tags to our 4 buckets (best-effort keyword match). */
    private fun mapCategory(tags: List<String>?): String {
        val joined = tags?.joinToString(" ")?.lowercase() ?: return "OTHER"
        return when {
            listOf("dairy", "dairies", "milk", "cheese", "yogurt", "yoghurt", "butter")
                .any { joined.contains(it) } -> "DAIRY"
            listOf("meat", "poultry", "fish", "seafood", "beef", "chicken", "pork", "sausage")
                .any { joined.contains(it) } -> "MEAT"
            listOf("fruit", "vegetable", "produce", "legume", "salad", "herb")
                .any { joined.contains(it) } -> "PRODUCE"
            else -> "OTHER"
        }
    }
}