package com.example.habisin.data.remote.dto

data class OpenFoodResponse (
    val product: scanData?
)

data class scanData(
    val product_name: String?
)