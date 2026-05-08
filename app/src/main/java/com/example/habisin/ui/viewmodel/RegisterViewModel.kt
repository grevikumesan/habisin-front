package com.example.habisin.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.habisin.data.remote.container.AppContainer
import com.example.habisin.ui.uistate.RegisterUiState
import kotlinx.coroutines.launch

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val container = AppContainer(application)
    private val authRepo  = container.authRepository

    // ── Form state ─────────────────────────────────────────────────────────

    var name            by mutableStateOf(""); private set
    var email           by mutableStateOf(""); private set
    var password        by mutableStateOf(""); private set
    var confirmPassword by mutableStateOf(""); private set

    var registerUiState: RegisterUiState by mutableStateOf(RegisterUiState.Idle)
        private set

    // ── Form handlers ──────────────────────────────────────────────────────

    fun onNameChange(value: String)            { name            = value }
    fun onEmailChange(value: String)           { email           = value }
    fun onPasswordChange(value: String)        { password        = value }
    fun onConfirmPasswordChange(value: String) { confirmPassword = value }

    // ── Actions ────────────────────────────────────────────────────────────

    fun register() {
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            registerUiState = RegisterUiState.Error("Please fill in all fields.")
            return
        }
        if (password != confirmPassword) {
            registerUiState = RegisterUiState.Error("Passwords do not match.")
            return
        }
        viewModelScope.launch {
            registerUiState = RegisterUiState.Loading
            try {
                val response = authRepo.register(name.trim(), email.trim(), password)
                if (response.isSuccessful) {
                    val token = response.body()!!.data.token
                    registerUiState = RegisterUiState.Success(token)
                } else {
                    // TODO (backend team): parse response.errorBody() with Gson here
                    // for a more specific error message from your Express server.
                    registerUiState = RegisterUiState.Error("Registration failed (${response.code()}).")
                }
            } catch (e: Exception) {
                registerUiState = RegisterUiState.Error(e.message ?: "Unexpected error. Check your connection.")
            }
        }
    }

    fun resetState() {
        registerUiState = RegisterUiState.Idle
        name            = ""
        email           = ""
        password        = ""
        confirmPassword = ""
    }
}