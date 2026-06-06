package com.example.habisin.data.remote.dto

data class ResepResponse(
    val id: Int,
    val resepName: String,
    val resepDescription: String,
    val resepCategory: String,
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

// ── Catalog (browse) — GET /api/catalog, /catalog/:id, /catalog/categories ──
data class CatalogItem(
    val id: Int,
    val name: String? = null,
    val description: String? = null,
    val category: String? = null,
    val imageUrl: String? = null,
    val isPremium: Boolean = false,
    val locked: Boolean = false,
    val ingredients: List<String> = emptyList(),  // populated on detail
    val directions: List<String> = emptyList()
)

data class CatalogPagination(
    val total: Int = 0,
    val page: Int = 1,
    val limit: Int = 10,
    val totalPages: Int = 1
)

data class CatalogListResponse(
    val success: Boolean = false,
    val data: List<CatalogItem> = emptyList(),
    val pagination: CatalogPagination? = null
)

data class CatalogDetailResponse(
    val success: Boolean = false,
    val data: CatalogItem? = null
)

data class CatalogCategoriesResponse(
    val success: Boolean = false,
    val data: List<String> = emptyList()
)