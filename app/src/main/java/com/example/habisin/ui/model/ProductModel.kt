package com.example.habisin.ui.model

import com.example.habisin.util.daysFromToday
import com.google.gson.annotations.SerializedName
import java.util.Date

data class ProductModel(
    val id: Int = 0,

    @SerializedName("foodName")
    val name: String = "",

    @SerializedName("descriptionFood")
    val description: String = "",

    val category: String = "OTHER",

    @SerializedName("bestBefore")
    val bestBeforeDate: Date? = null,

    val daysLeft: Int = 0,                  // dari BE, fallback kalau ada
    val quantity: Int = 0,
    val unit: String = "PCS",
    val imageUrl: String = ""
) {
    val computedDaysLeft: Int
        get() = bestBeforeDate?.daysFromToday() ?: daysLeft
}

data class DashboardModel(
    val expiringFoods: List<ProductModel> = emptyList(),
    val totalItems: Int = 0,
    val expiringTotal: Int = 0
)