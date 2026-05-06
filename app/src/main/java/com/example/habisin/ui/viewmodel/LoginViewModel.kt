package com.example.habisin.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.habisin.data.container.AuthContainer
import com.example.habisin.ui.model.UserModel
import com.example.habisin.ui.uistate.LoginUiState
import kotlinx.coroutines.launch

/**
 * Uses AndroidViewModel (not plain ViewModel) so it can access Application context,
 * which AuthContainer needs to initialise SessionManager.
 */
class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val container  = AuthContainer(application)
    private val authRepo   = container.authRepository
    val sessionManager     = container.sessionManager  // AppRouter reads this to check isLoggedIn()

    // ── Form state ─────────────────────────────────────────────────────────

    var email    by mutableStateOf(""); private set
    var password by mutableStateOf(""); private set

    var loginUiState: LoginUiState by mutableStateOf(LoginUiState.Idle)
        private set

    // ── Form handlers ──────────────────────────────────────────────────────

    fun onEmailChange(value: String)    { email    = value }
    fun onPasswordChange(value: String) { password = value }

    // ── Actions ────────────────────────────────────────────────────────────

    fun login() {
        if (email.isBlank() || password.isBlank()) {
            loginUiState = LoginUiState.Error("Please fill in all fields.")
            return
        }
        viewModelScope.launch {
            loginUiState = LoginUiState.Loading
            try {
                val response = authRepo.login(email.trim(), password)
                if (response.isSuccessful) {
                    val token = response.body()!!.data.token
                    loginUiState = LoginUiState.Success(token)
                } else {
                    // TODO (backend team): parse response.errorBody() with Gson here
                    // for a more specific error message from your Express server.
                    loginUiState = LoginUiState.Error("Login failed (${response.code()}). Check your credentials.")
                }
            } catch (e: Exception) {
                loginUiState = LoginUiState.Error(e.message ?: "Unexpected error. Check your connection.")
            }
        }
    }

    /** Call after navigating away so state resets if the user comes back to this screen. */
    fun resetState() {
        loginUiState = LoginUiState.Idle
        email        = ""
        password     = ""
    }
}