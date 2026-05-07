package com.example.habisin.ui.viewmodel

//import androidx.lifecycle.ViewModel
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import com.habisin.front.data.model.ProductModel
//import java.util.*
//import java.util.concurrent.TimeUnit
//
//class MyFridgeViewModel : ViewModel() {
//
//    private val _uiState = MutableStateFlow(FridgeUiState())
//    val uiState: StateFlow<FridgeUiState> = _uiState.asStateFlow()
//
//    // Sample data - replace with actual data source
//    private val sampleProducts = listOf(
//        ProductModel(
//            id = "1",
//            name = "Eggs",
//            category = "Dairy",
//            bestBeforeDate = Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1)),
//            daysLeft = 1,
//            quantity = 3,
//            unit = "PCS",
//            imageUrl = ""
//        ),
//        ProductModel(
//            id = "2",
//            name = "Bread",
//            category = "Other",
//            bestBeforeDate = Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(2)),
//            daysLeft = 2,
//            quantity = 2,
//            unit = "PCS",
//            imageUrl = ""
//        ),
//        ProductModel(
//            id = "3",
//            name = "Apple",
//            category = "Produce",
//            bestBeforeDate = Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(2)),
//            daysLeft = 2,
//            quantity = 2,
//            unit = "PCS",
//            imageUrl = ""
//        ),
//        ProductModel(
//            id = "4",
//            name = "Milk",
//            category = "Dairy",
//            bestBeforeDate = Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(4)),
//            daysLeft = 4,
//            quantity = 1,
//            unit = "L",
//            imageUrl = ""
//        ),
//        ProductModel(
//            id = "5",
//            name = "Carrots",
//            category = "Produce",
//            bestBeforeDate = Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(7)),
//            daysLeft = 7,
//            quantity = 5,
//            unit = "PCS",
//            imageUrl = ""
//        ),
//        ProductModel(
//            id = "6",
//            name = "Spinach",
//            category = "Produce",
//            bestBeforeDate = Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(8)),
//            daysLeft = 8,
//            quantity = 1,
//            unit = "PACK",
//            imageUrl = ""
//        )
//    )
//
//    init {
//        _uiState.value = _uiState.value.copy(products = sampleProducts)
//        updateFilteredProducts()
//    }
//
//    fun onSearchQueryChange(query: String) {
//        _uiState.value = _uiState.value.copy(searchQuery = query)
//        updateFilteredProducts()
//    }
//
//    fun onCategorySelected(category: String) {
//        _uiState.value = _uiState.value.copy(selectedCategory = category)
//        updateFilteredProducts()
//    }
//
//    private fun updateFilteredProducts() {
//        val state = _uiState.value
//        var filtered = state.products
//
//        // Filter by search query
//        if (state.searchQuery.isNotBlank()) {
//            filtered = filtered.filter {
//                it.name.contains(state.searchQuery, ignoreCase = true)
//            }
//        }
//
//        // Filter by category
//        if (state.selectedCategory != "All") {
//            if (state.selectedCategory == "Expiring") {
//                filtered = filtered.filter { it.daysLeft <= 3 }
//            } else {
//                filtered = filtered.filter {
//                    it.category.equals(state.selectedCategory, ignoreCase = true)
//                }
//            }
//        }
//
//        // Get expiring products (<= 3 days)
//        val expiringProducts = state.products.filter { it.daysLeft <= 3 }
//
//        _uiState.value = state.copy(
//            filteredProducts = filtered,
//            expiringProducts = expiringProducts
//        )
//    }
//}