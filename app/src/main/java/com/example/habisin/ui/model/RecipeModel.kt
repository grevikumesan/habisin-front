package com.example.habisin.ui.model

data class RecipeModel(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String,
    val ingredients: List<String>,
    val category: String,
    val cookTime: String,
    val difficulty: String,
    val servings: Int = 2,
    val calories: Int = 0,
    val instructions: List<String> = emptyList(),
    val tags: List<String> = emptyList(),
    val rating: Float = 0f,
    val reviewCount: Int = 0,
    val author: String = "",
    val datePublished: String = ""
)