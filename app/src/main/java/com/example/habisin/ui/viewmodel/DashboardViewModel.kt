package com.example.habisin.ui.viewmodel

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.habisin.data.local.SettingsManager
import com.example.habisin.data.remote.container.AppContainer
import com.example.habisin.ui.uistate.DashboardUiState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DashboardViewModel(app: Application) : AndroidViewModel(app) {

    private val container = AppContainer(app)
    private val settings  = SettingsManager(app)

    var dashboardUiState: DashboardUiState by mutableStateOf(DashboardUiState.Loading)
        private set

    init {
        loadDashboard()
    }

    fun loadDashboard() {
        viewModelScope.launch {
            dashboardUiState = DashboardUiState.Loading

            val username   = "Hans"
            val profilePic = settings.profilePictureFlow.first()

            container.dashboardRepository.getDashboard()
                .onSuccess { response ->
                    Log.d("Dashboard", "totalItems: ${response.totalItems}")
                    Log.d("Dashboard", "expiringTotal: ${response.expiringTotal}")
                    response.expiringFoods.forEach { product ->
                        Log.d("Dashboard", "Product: id=${product.id}, name='${product.name}', daysLeft=${product.daysLeft}, category=${product.category}")
                    }

                    dashboardUiState = DashboardUiState.Success(
                        username          = username,
                        profilePictureUri = profilePic,
                        expiringItems     = response.expiringFoods,
                        totalItems        = response.totalItems,
                        expiringTotal     = response.expiringTotal
                    )
                }
                .onFailure { error ->
                    Log.e("Dashboard", "Error loading dashboard", error)
                    dashboardUiState = DashboardUiState.Error(
                        error.message ?: "Failed to load dashboard"
                    )
                }
        }
    }

    fun updateProfilePicture(uri: Uri) {
        viewModelScope.launch {
            getApplication<Application>().contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            settings.setProfilePicture(uri.toString())
            loadDashboard()
        }
    }
}