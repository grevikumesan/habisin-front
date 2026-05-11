package com.example.habisin.ui.uistate

import java.util.Date

sealed interface AddProductScanUiStates {
    data object Idle: AddProductScanUiStates
    data object Loading: AddProductScanUiStates

    data class Success(
        val itemName: String = "",
        val description: String = "",
        val category: String = "OTHER",
        val bestBeforeDate: Date? = null,
        val daysLeft: Int = 0,
        val quantity: Int = 1
    ) : AddProductScanUiStates

    data class Error(
        val message: String
    ) : AddProductScanUiStates
}