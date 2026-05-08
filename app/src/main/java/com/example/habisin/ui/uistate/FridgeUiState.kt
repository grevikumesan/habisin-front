package com.example.habisin.ui.uistate

import com.example.habisin.ui.model.ProductModel

data class FridgeUiState(
    val searchQuery: String = "",
    val selectedCategory: String = "All",
    val products: List<ProductModel> = emptyList(),
    val filteredProducts: List<ProductModel> = emptyList(),
    val expiringProducts: List<ProductModel> = emptyList()
)