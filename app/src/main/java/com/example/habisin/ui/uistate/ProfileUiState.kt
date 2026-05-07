package com.example.habisin.ui.uistate

sealed interface ProfileUiState {
    data object Loading : ProfileUiState
    data class Success(
        val username: String,
        val email: String
    ) : ProfileUiState
    data class Error(val message: String) : ProfileUiState
}