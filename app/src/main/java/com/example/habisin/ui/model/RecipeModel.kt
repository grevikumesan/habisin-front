package com.example.habisin.ui.model

data class RecipeModel(
    val id: Int,
    val resepName: String,
    val resepDescription: String,
    val resepCategory: String = "",
    val resepIngredients: List<String> = emptyList(),
    val resepDirections: List<String> = emptyList()
)