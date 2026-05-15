package com.example.habisin.ui.uistate

import com.example.habisin.ui.model.RecipeModel

data class RecipeDetailUiState(
    val isLoading: Boolean = false,
    val recipe: RecipeModel? = null,
    val isGenerating: Boolean = false,
    val errorMessage: String? = null
)