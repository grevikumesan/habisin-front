package com.example.habisin.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.habisin.data.remote.container.AppContainer
import com.example.habisin.ui.model.ProductModel
import com.example.habisin.ui.uistate.FridgeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MyFridgeViewModel(app: Application) : AndroidViewModel(app) {

    private val container = AppContainer(app)

    private val _uiState = MutableStateFlow(FridgeUiState())
    val uiState: StateFlow<FridgeUiState> = _uiState.asStateFlow()

    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            container.foodRepository.getAll()
                .onSuccess { products ->
                    val state = _uiState.value
                    _uiState.value = state.copy(
                        isLoading        = false,
                        products         = products,
                        filteredProducts = applyFilters(products, state.selectedCategory, state.searchQuery),
                        expiringProducts = products.filter { it.computedDaysLeft <= 3 }   // ← ini
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading    = false,
                        errorMessage = error.message ?: "Failed to load products"
                    )
                }
        }
    }

    fun onSearchQueryChange(query: String) {
        val state = _uiState.value
        _uiState.value = state.copy(
            searchQuery      = query,
            filteredProducts = applyFilters(state.products, state.selectedCategory, query)
        )
    }

    fun onCategorySelected(category: String) {
        val state = _uiState.value
        _uiState.value = state.copy(
            selectedCategory = category,
            filteredProducts = applyFilters(state.products, category, state.searchQuery)
        )
    }

    private fun applyFilters(
        products: List<ProductModel>,
        category: String,
        query: String
    ): List<ProductModel> {
        var result = products

        result = when (category) {
            "All"      -> result
            "Expiring" -> result.filter { it.computedDaysLeft <= 3 }
            else       -> result.filter { it.category.equals(category, ignoreCase = true) }
        }

        if (query.isNotBlank()) {
            result = result.filter { it.name.contains(query, ignoreCase = true) }
        }

        return result.sortedBy { it.computedDaysLeft }
    }
}