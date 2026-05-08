package com.example.habisin.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habisin.ui.uistate.ProfileUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    var profileUiState: ProfileUiState by mutableStateOf(ProfileUiState.Loading)
        private set

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            profileUiState = ProfileUiState.Loading
            // TODO (teammates): ganti pakai SessionManager / API call beneran
            delay(300)
            profileUiState = ProfileUiState.Success(
                username = "Hans Vere Liem",
                email    = "vereliemhans@gmail.com"
            )
        }
    }

    fun logout(onLoggedOut: () -> Unit) {
        viewModelScope.launch {
            // TODO (teammates): bersihin session token / DataStore
            onLoggedOut()
        }
    }
}