package com.example.habisin.ui.uistate

import com.example.habisin.ui.model.RecipeModel

data class RecipeUiState(
    val recipes: List<RecipeModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val selectedCategory: String = "All"
)