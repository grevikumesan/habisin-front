package com.example.habisin.ui.model

import java.util.Date

// Representasi data bahan makanan di kulkas
data class ProductModel(
    val id: String,
    val name: String,
    val category: String, // "Produce", "Dairy", "Meat", "Other"
    val bestBeforeDate: Date,
    val daysLeft: Int,
    val quantity: Int,
    val unit: String,
    val imageUrl: String // Nullable karena bisa pakai default icon kalau ga ada foto
)