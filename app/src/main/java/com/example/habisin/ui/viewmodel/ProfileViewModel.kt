package com.example.habisin.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.habisin.data.remote.container.AppContainer
import com.example.habisin.ui.uistate.ProfileUiState
import com.example.habisin.util.decodeJwtClaims
import kotlinx.coroutines.launch

class ProfileViewModel(app: Application) : AndroidViewModel(app) {

    private val container = AppContainer(app)
    private val sessionManager = container.sessionManager
    private val authRepository = container.authRepository

    var profileUiState: ProfileUiState by mutableStateOf(ProfileUiState.Loading)
        private set

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            profileUiState = ProfileUiState.Loading

            // Prefer info yg disimpan saat login/register; fallback decode token yg lagi aktif
            // (buat sesi lama yg ke-save sebelum fitur ini ada).
            val claims = decodeJwtClaims(sessionManager.getToken())
            val email = sessionManager.getEmail() ?: claims.email
            val username = sessionManager.getUsername()
                ?: claims.username
                ?: email?.substringBefore("@")?.replaceFirstChar { it.uppercase() }

            profileUiState = if (email.isNullOrBlank() && username.isNullOrBlank()) {
                ProfileUiState.Error("Belum login")
            } else {
                ProfileUiState.Success(
                    username = username ?: "User",
                    email    = email ?: ""
                )
            }
        }
    }

    fun logout(onLoggedOut: () -> Unit) {
        viewModelScope.launch {
            authRepository.logout()   // clear token + user info dari DataStore
            onLoggedOut()
        }
    }
}
