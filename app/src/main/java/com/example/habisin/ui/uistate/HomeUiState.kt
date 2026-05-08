package com.example.habisin.ui.uistate

import com.example.habisin.ui.model.ProductModel
import com.example.habisin.ui.model.RecipeModel

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(
        val userName: String,
        val expiringItems: List<ProductModel>, // Items < 3 days
        val totalItemsCount: Int,
        val expiringItemsCount: Int,
        val recipeOfTheDay: RecipeModel?
    ) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}