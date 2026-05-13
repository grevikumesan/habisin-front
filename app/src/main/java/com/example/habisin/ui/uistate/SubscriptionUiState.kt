package com.example.habisin.ui.uistate

data class SubscriptionUiState(
    val isLoading: Boolean = false,
    val isActive: Boolean = false,
    val expiresAt: String? = null,
    val daysRemaining: Int = 0,
    val redirectUrl: String? = null,  // di-set saat user klik subscribe
    val orderId: String? = null,
    val errorMessage: String? = null,
    val showWebView: Boolean = false
)