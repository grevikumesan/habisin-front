package com.example.habisin.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.habisin.data.remote.container.AppContainer
import com.example.habisin.ui.model.RecipeModel
import com.example.habisin.ui.uistate.RecipeDetailUiState
import com.example.habisin.ui.uistate.RecipeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RecipeViewModel(app: Application) : AndroidViewModel(app) {

    private val container = AppContainer(app)
    private val repository = container.recipeRepository

    private val _uiState = MutableStateFlow(RecipeUiState())
    val uiState: StateFlow<RecipeUiState> = _uiState.asStateFlow()

    private val _detailUiState = MutableStateFlow(RecipeDetailUiState())
    val detailUiState: StateFlow<RecipeDetailUiState> = _detailUiState.asStateFlow()

    private val _generatedRecipeId = MutableStateFlow<Int?>(null)
    val generatedRecipeId: StateFlow<Int?> = _generatedRecipeId.asStateFlow()

    fun clearGeneratedRecipeId() {
        _generatedRecipeId.value = null
    }

// ─── GET ALL ───────────────────────────────────────────
    fun loadRecipes() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                val response = repository.getAllResep()
                if (response.isSuccessful) {
                    val recipes = response.body()?.data?.map {
                        RecipeModel(
                            id = it.id,
                            resepName = it.resepName,
                            resepDescription = it.resepDescription,
                            resepCategory = it.resepCategory,
                            resepIngredients = it.resepIngredients,
                            resepDirections = it.resepDirections
                        )
                    } ?: emptyList()

                    android.util.Log.d("RECIPE_VM", "Success, ${recipes.size} recipes loaded, needsSubscription set to false")

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        recipes = recipes,
                        needsSubscription = false  // ← penting! clear flag dari fetch sebelumnya
                    )
                } else {
                    val errorBody = response.errorBody()?.string()
                    val isSubRequired = response.code() == 403 &&
                            errorBody?.contains("\"subscriptionRequired\":true") == true

                    android.util.Log.d("RECIPE_VM", "Failed: code=${response.code()}, isSubRequired=$isSubRequired")

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = if (isSubRequired) null else "Gagal memuat resep (${response.code()})",
                        needsSubscription = isSubRequired
                    )
                }
            } catch (e: Exception) {
                android.util.Log.e("RECIPE_VM", "Exception: ${e.message}")
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Terjadi kesalahan"
                )
            }
        }
    }

    // ─── GET BY ID ─────────────────────────────────────────
    fun getResepById(id: Int) {
        viewModelScope.launch {
            _detailUiState.value = _detailUiState.value.copy(isLoading = true, errorMessage = null)
            try {
                val response = repository.getResepById(id)
                if (response.isSuccessful) {
                    val data = response.body()?.data
                    val recipe = data?.let {
                        RecipeModel(
                            id = it.id,
                            resepName = it.resepName,
                            resepDescription = it.resepDescription,
                            resepCategory = it.resepCategory,
                            resepIngredients = it.resepIngredients,
                            resepDirections = it.resepDirections
                        )
                    }
                    _detailUiState.value = _detailUiState.value.copy(isLoading = false, recipe = recipe)
                } else {
                    _detailUiState.value = _detailUiState.value.copy(
                        isLoading = false,
                        errorMessage = "Resep tidak ditemukan (${response.code()})"
                    )
                }
            } catch (e: Exception) {
                _detailUiState.value = _detailUiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Terjadi kesalahan"
                )
            }
        }
    }

    // ─── GENERATE (AI) ─────────────────────────────────────
    fun generateResep(saveToHistory: Boolean = false) {
        viewModelScope.launch {
            _detailUiState.value = _detailUiState.value.copy(isGenerating = true, errorMessage = null)
            try {
                val response = repository.generateResep(saveToHistory)
                if (response.isSuccessful) {
                    val body = response.body()
                    val data = body?.saved ?: body?.aiResponse  // ← prefer saved (has real DB id)

                    val recipe = data?.let {
                        RecipeModel(
                            id = it.id,
                            resepName = it.resepName,
                            resepDescription = it.resepDescription,
                            resepCategory = it.resepCategory,
                            resepIngredients = it.resepIngredients,
                            resepDirections = it.resepDirections
                        )
                    }
                    _generatedRecipeId.value = recipe?.id
                    _detailUiState.value = _detailUiState.value.copy(isGenerating = false, recipe = recipe)
                } else {
                    _detailUiState.value = _detailUiState.value.copy(
                        isGenerating = false,
                        errorMessage = "Gagal generate resep (${response.code()})"
                    )
                }
            } catch (e: Exception) {
                _detailUiState.value = _detailUiState.value.copy(
                    isGenerating = false,
                    errorMessage = e.message ?: "Terjadi kesalahan"
                )
            }
        }
    }

    // ─── DELETE ────────────────────────────────────────────
    fun deleteResep(id: Int) {
        viewModelScope.launch {
            try {
                val response = repository.deleteResep(id)
                if (response.isSuccessful) {
                    loadRecipes() // refresh list setelah delete
                } else {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Gagal menghapus resep (${response.code()})"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message)
            }
        }
    }

    // ─── SEARCH ────────────────────────────────────────────
    fun onSearchQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
        _detailUiState.value = _detailUiState.value.copy(errorMessage = null)
    }
}