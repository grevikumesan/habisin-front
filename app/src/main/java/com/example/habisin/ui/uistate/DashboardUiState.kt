package com.example.habisin.ui.uistate

import com.example.habisin.ui.model.ProductModel

sealed interface DashboardUiState {
    data object Loading : DashboardUiState
    data class Success(
        val username: String,
        val profilePictureUri: String?,
        val expiringItems: List<ProductModel>,    // = response.expiringFood
        val totalItems: Int,                      // = response.totalItems
        val expiringTotal: Int                    // = response.expiringTotal (ganti dari .size)
    ) : DashboardUiState
    data class Error(val message: String) : DashboardUiState
}