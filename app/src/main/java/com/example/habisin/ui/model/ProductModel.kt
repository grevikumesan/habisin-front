package com.example.habisin.ui.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class ProductModel(
    val id: Int,

    @SerializedName("foodName")
    val name: String,

    @SerializedName("descriptionFood")
    val description: String = "",

    val category: String,

    @SerializedName("bestBefore")
    val bestBeforeDate: Date,

    val daysLeft: Int = 0,                           // dihitung server, default 0 kalau ga ada
    val quantity: Int,

    val unit: String = "PCS",                        // BE ga punya, default
    val imageUrl: String = ""                        // BE ga punya, default
)

data class DashboardModel(
    val expiringFoods: List<ProductModel>,
    val totalItems: Int,
    val expiringTotal: Int
)