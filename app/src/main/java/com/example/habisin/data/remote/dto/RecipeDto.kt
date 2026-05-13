package com.example.habisin.data.remote.dto

data class ResepResponse(
    val id: Int,
    val resepName: String,
    val resepDescription: String,
    val resepIngredients: List<String>,
    val resepDirections: List<String>
)

data class ResepListResponse(
    val success: Boolean,
    val data: List<ResepResponse>
)

data class ResepDetailResponse(
    val success: Boolean,
    val data: ResepResponse
)

data class ResepGenerateResponse(
    val success: Boolean,
    val aiResponse: ResepResponse,
    val saved: ResepResponse? = null
)

data class ResepGenerateRequest(
    val saveToHistory: Boolean = false
)

data class ResepDeleteResponse(
    val success: Boolean,
    val message: String
)