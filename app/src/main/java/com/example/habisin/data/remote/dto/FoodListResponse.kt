package com.example.habisin.data.remote.dto

import com.example.habisin.ui.model.ProductModel

data class FoodListResponse(
    val success: Boolean,
    val message: String,
    val data: List<ProductModel>
)