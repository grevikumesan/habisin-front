package com.example.habisin.ui.uistate
sealed class RegisterUiState {
    object Idle    : RegisterUiState()
    object Loading : RegisterUiState()
    data class Success(val token: String) : RegisterUiState()
    data class Error(val message: String) : RegisterUiState()
}