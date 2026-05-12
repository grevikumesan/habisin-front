package com.example.habisin.ui.model

data class DashboardModel(
    val expiringFoods: List<ProductModel>,
    val totalItems: Int,
    val expiringTotal: Int
)