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

// ─── BROWSE CATALOG (the 16 seeded recipes shown on the Recipe tab) ───────
    fun loadRecipes() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                // Fetch the whole catalog (limit high enough for all of it); the screen
                // does its own search/category filtering client-side.
                val response = repository.getCatalog(page = 1, limit = 100)
                if (response.isSuccessful) {
                    val recipes = response.body()?.data?.map {
                        RecipeModel(
                            id = it.id,
                            resepName = it.name ?: "",
                            resepDescription = it.description ?: "",
                            resepCategory = it.category ?: "",
                            imageUrl = it.imageUrl,
                            resepIngredients = it.ingredients ?: emptyList(),
                            resepDirections = it.directions ?: emptyList()
                        )
                    } ?: emptyList()

                    android.util.Log.d("RECIPE_VM", "Catalog loaded: ${recipes.size} recipes")

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        recipes = recipes,
                        needsSubscription = false
                    )
                } else {
                    android.util.Log.d("RECIPE_VM", "Catalog failed: code=${response.code()}")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Gagal memuat resep (${response.code()})"
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

    // ─── SAVED RECIPES (user's generated/saved, from /api/resep/all) ───
    fun loadSavedRecipes() {
        viewModelScope.launch {
            try {
                val response = repository.getAllResep()
                if (response.isSuccessful) {
                    val saved = response.body()?.data?.map {
                        RecipeModel(
                            id = it.id,
                            resepName = it.resepName,
                            resepDescription = it.resepDescription,
                            resepCategory = it.resepCategory,
                            resepIngredients = it.resepIngredients,
                            resepDirections = it.resepDirections
                        )
                    } ?: emptyList()
                    _uiState.value = _uiState.value.copy(savedRecipes = saved)
                }
            } catch (e: Exception) {
                android.util.Log.e("RECIPE_VM", "Saved load failed: ${e.message}")
            }
        }
    }

    // ─── CATALOG DETAIL (ingredients + directions) ─────────
    fun loadCatalogDetail(id: Int) {
        viewModelScope.launch {
            _detailUiState.value = _detailUiState.value.copy(isLoading = true, errorMessage = null)
            try {
                val response = repository.getCatalogById(id)
                if (response.isSuccessful) {
                    val data = response.body()?.data
                    val recipe = data?.let {
                        RecipeModel(
                            id = it.id,
                            resepName = it.name ?: "",
                            resepDescription = it.description ?: "",
                            resepCategory = it.category ?: "",
                            imageUrl = it.imageUrl,
                            resepIngredients = it.ingredients ?: emptyList(),
                            resepDirections = it.directions ?: emptyList()
                        )
                    }
                    _detailUiState.value = _detailUiState.value.copy(isLoading = false, recipe = recipe)
                } else {
                    val isSubRequired = response.code() == 403
                    _detailUiState.value = _detailUiState.value.copy(
                        isLoading = false,
                        errorMessage = if (isSubRequired) "Resep ini khusus PRO. Upgrade untuk membukanya."
                                       else "Resep tidak ditemukan (${response.code()})"
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