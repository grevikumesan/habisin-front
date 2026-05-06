package com.example.habisin.ui.uistate

import com.example.habisin.ui.model.ProductModel

sealed class FridgeUiState {
    object Loading : FridgeUiState()
    data class Success(
        val searchQuery: String = "",
        val selectedCategory: String = "All",
        val products: List<ProductModel>
    ) : FridgeUiState()
    data class Error(val message: String) : FridgeUiState()
}