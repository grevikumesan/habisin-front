package com.example.habisin.ui.uistate

import com.example.habisin.ui.model.UserModel

/**
 * LoginUiState — all possible states for the Login screen.
 *
 * Idle    → initial, nothing happening
 * Loading → API call in-flight, show spinner / disable button
 * Success → login succeeded, navigate away
 * Error   → login failed, show message to user
 */
sealed class LoginUiState {
    object Idle    : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val token: String) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

