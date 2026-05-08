package com.example.habisin.ui.viewmodel

import androidx.lifecycle.ViewModel

import com.example.habisin.ui.uistate.RecipeDetailUiState
import com.example.habisin.ui.uistate.RecipeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RecipeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(RecipeUiState())
    val uiState: StateFlow<RecipeUiState> = _uiState.asStateFlow()

    private val _detailUiState = MutableStateFlow(RecipeDetailUiState())
    val detailUiState: StateFlow<RecipeDetailUiState> = _detailUiState.asStateFlow()

    fun onSearchQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        // Logic filter atau hit API di sini
    }

    fun selectCategory(category: String) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    fun getRecipeById(id: String) {
        // Simulasi ambil data detail
        _detailUiState.value = _detailUiState.value.copy(isLoading = true)
        // Nanti panggil repository.getRecipe(id)
    }
}