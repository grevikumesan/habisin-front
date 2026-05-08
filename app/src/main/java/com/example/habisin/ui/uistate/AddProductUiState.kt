package com.example.habisin.ui.uistate

import java.util.Date

data class AddProductUiState(
    val itemName: String = "",
    val category: String = "PRODUCE",
    val bestBeforeDate: Date? = null,
    val daysLeft: Int = 0,
    val quantity: Int = 1,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)