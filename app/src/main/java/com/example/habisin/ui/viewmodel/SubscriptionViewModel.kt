package com.example.habisin.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.habisin.data.remote.container.AppContainer
import com.example.habisin.ui.uistate.SubscriptionUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SubscriptionViewModel(app: Application) : AndroidViewModel(app) {

    private val container = AppContainer(app)
    private val repository = container.paymentRepository

    private val _uiState = MutableStateFlow(SubscriptionUiState())
    val uiState: StateFlow<SubscriptionUiState> = _uiState.asStateFlow()

    fun loadStatus() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                val response = repository.getStatus()
                if (response.isSuccessful) {
                    val data = response.body()?.data
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isActive = data?.isActive ?: false,
                        expiresAt = data?.expiresAt,
                        daysRemaining = data?.daysRemaining ?: 0
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Gagal memuat status (${response.code()})"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Terjadi kesalahan"
                )
            }
        }
    }

    /**
     * Klik tombol "Berlangganan" → minta backend bikin transaksi Midtrans.
     * Setelah dapat redirectUrl, buka WebView.
     */
    fun subscribe() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                android.util.Log.d("SUB", "Calling subscribe API...")
                val response = repository.subscribe()
                android.util.Log.d("SUB", "Response code: ${response.code()}")
                android.util.Log.d("SUB", "Response success: ${response.isSuccessful}")
                android.util.Log.d("SUB", "Response body: ${response.body()}")
                android.util.Log.d("SUB", "RedirectUrl: ${response.body()?.data?.redirectUrl}")

                if (response.isSuccessful) {
                    val data = response.body()?.data
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        redirectUrl = data?.redirectUrl,
                        orderId = data?.orderId,
                        showWebView = data?.redirectUrl != null
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Gagal membuat transaksi (${response.code()})"
                    )
                }
            } catch (e: Exception) {
                android.util.Log.e("SUB", "Error: ${e.message}", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Terjadi kesalahan"
                )
            }
        }
    }

    /**
     * Dipanggil saat WebView ditutup (user selesai bayar / cancel).
     * Refresh status dari backend (source of truth).
     */
    fun onWebViewClosed() {
        _uiState.value = _uiState.value.copy(
            showWebView = false,
            redirectUrl = null
        )

        // Polling 3x dengan delay biar yakin status sync dengan webhook
        viewModelScope.launch {
            repeat(3) { attempt ->
                android.util.Log.d("SUB", "Polling status, attempt ${attempt + 1}")
                loadStatusSilent()

                // Cek apakah status udah active
                if (_uiState.value.isActive) {
                    android.util.Log.d("SUB", "Status active! Stop polling")
                    return@launch
                }

                // Delay sebelum retry (kecuali attempt terakhir)
                if (attempt < 2) {
                    kotlinx.coroutines.delay(1500)
                }
            }
        }
    }

    private suspend fun loadStatusSilent() {
        try {
            val response = repository.getStatus()
            if (response.isSuccessful) {
                val data = response.body()?.data
                _uiState.value = _uiState.value.copy(
                    isActive = data?.isActive ?: false,
                    expiresAt = data?.expiresAt,
                    daysRemaining = data?.daysRemaining ?: 0
                )
            }
        } catch (e: Exception) {
            android.util.Log.e("SUB", "Silent poll error: ${e.message}")
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}