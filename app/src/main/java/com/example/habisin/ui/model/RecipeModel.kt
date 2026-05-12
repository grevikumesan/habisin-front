package com.example.habisin.ui.model

// Representasi data resep rekomendasi
data class RecipeModel(
    val id: String,
    val title: String,
    val timeToCook: String, // e.g., "30 mins"
    val difficulty: String, // e.g., "Easy", "Medium"
    val imageUrl: String,
    val description: String,
    val ingredients: List<String> = emptyList(),
    val directions: List<String> = emptyList()
)