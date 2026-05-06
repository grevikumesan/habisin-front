package com.example.habisin.ui.uistate

import com.example.habisin.ui.model.UserModel

/**
 * RegisterUiState — all possible states for the Register screen.
 *
 * Idle    → initial state
 * Loading → API call in-flight
 * Success → registration succeeded
 * Error   → registration failed, show message
 */
sealed class RegisterUiState {
    object Idle    : RegisterUiState()
    object Loading : RegisterUiState()
    data class Success(val token: String) : RegisterUiState()
    data class Error(val message: String) : RegisterUiState()
}