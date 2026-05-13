package com.example.habisin.data.remote.dto

data class SubscribeData(
    val orderId: String,
    val snapToken: String,
    val redirectUrl: String,
    val amount: Int,
    val currentExpiresAt: String? = null
)

data class SubscribeResponse(
    val success: Boolean,
    val data: SubscribeData
)

data class SubscriptionStatusData(
    val isActive: Boolean,
    val expiresAt: String? = null,
    val daysRemaining: Int = 0
)

data class SubscriptionStatusResponse(
    val success: Boolean,
    val data: SubscriptionStatusData
)