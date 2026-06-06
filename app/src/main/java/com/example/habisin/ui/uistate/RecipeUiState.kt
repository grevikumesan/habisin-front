package com.example.habisin.ui.uistate

import com.example.habisin.ui.model.RecipeModel

data class RecipeUiState(
    val isLoading: Boolean = false,
    val recipes: List<RecipeModel> = emptyList(),       // catalog (browse)
    val savedRecipes: List<RecipeModel> = emptyList(),  // user's saved/generated
    val searchQuery: String = "",
    val errorMessage: String? = null,
    val needsSubscription: Boolean = false
)