package com.example.habisin.ui.uistate

import java.util.*

data class AddProductUiState(
    val itemName: String = "",
    val category: String = "Produce",
    val bestBeforeDate: Date? = null,
    val daysLeft: Int = 0,
    val quantity: Int = 1
)