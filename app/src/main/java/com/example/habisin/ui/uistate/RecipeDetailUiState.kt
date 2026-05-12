package com.example.habisin.ui.uistate

import com.example.habisin.ui.model.RecipeModel

data class RecipeUiState(
    val isLoading: Boolean = false,
    val recipes: List<RecipeModel> = emptyList(),
    val categories: List<String> = listOf("All", "Rice", "Meat", "Veggie", "Soup"),
    val selectedCategory: String = "All",
    val searchQuery: String = "",
    val errorMessage: String? = null
)