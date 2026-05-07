package com.example.habisin.ui.uistate

sealed class LoginUiState {
    object Idle    : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val token: String) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

