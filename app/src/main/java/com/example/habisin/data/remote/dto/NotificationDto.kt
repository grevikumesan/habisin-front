package com.example.habisin.data.remote.dto

// GET /api/notifications?days=N  ->  { success, message, data: { ... } }
data class NotificationFeedResponse(
    val data: NotificationFeedData? = null
)

data class NotificationFeedData(
    val threshold: Int = 3,
    val expiredCount: Int = 0,
    val expiringCount: Int = 0,
    val expired: List<NotificationItem> = emptyList(),
    val expiringSoon: List<NotificationItem> = emptyList()
)

data class NotificationItem(
    val foodName: String? = null,
    val name: String? = null,
    val daysLeft: Int = 0
) {
    val displayName: String get() = foodName ?: name ?: "Item"
}
