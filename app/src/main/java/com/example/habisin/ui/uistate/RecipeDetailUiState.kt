package com.example.habisin.ui.uistate

import com.example.habisin.ui.model.RecipeModel

data class RecipeDetailUiState(
    val recipe: RecipeModel? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val matchedIngredients: Pair<Int, Int> = Pair(0, 0)
)