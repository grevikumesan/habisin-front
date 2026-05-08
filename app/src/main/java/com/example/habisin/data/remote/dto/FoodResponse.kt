package com.example.habisin.data.remote.dto

import com.example.habisin.ui.model.ProductModel

data class FoodResponse(
    val success: Boolean,
    val message: String,
    val data: ProductModel
)