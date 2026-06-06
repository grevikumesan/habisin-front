package com.example.habisin.data.remote.dto

data class OpenFoodResponse (
    val product: scanData?
)

data class scanData(
    val product_name: String?,
    val image_front_url: String? = null,
    val image_url: String? = null,
    val categories_tags: List<String>? = null,
    val generic_name: String? = null,
    val brands: String? = null
)