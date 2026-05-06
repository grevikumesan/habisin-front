package com.example.habisin.ui.model



// Representasi data bahan makanan di kulkas
data class ProductModel(
    val id: String,
    val name: String,
    val category: String, // "Produce", "Dairy", "Meat", "Other"
    val bestBeforeDate: String,
    val daysLeft: Int,
    val quantity: Int,
    val imageUrl: String // Nullable karena bisa pakai default icon kalau ga ada foto
)