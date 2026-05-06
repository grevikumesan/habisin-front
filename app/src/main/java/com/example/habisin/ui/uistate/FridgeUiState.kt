package com.example.habisin.ui.uistate

import com.habisin.front.data.model.ProductModel
import java.util.*

data class FridgeUiState(
    val searchQuery: String = "",
    val selectedCategory: String = "All",
    val products: List<ProductModel> = emptyList(),
    val filteredProducts: List<ProductModel> = emptyList(),
    val expiringProducts: List<ProductModel> = emptyList()
)