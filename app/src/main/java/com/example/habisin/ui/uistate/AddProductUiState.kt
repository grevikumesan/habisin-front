package com.example.habisin.ui.uistate

import android.net.Uri // <-- 1. JANGAN LUPA IMPORT INI
import java.util.Date

data class AddProductUiState(
    val itemName: String = "",
    val category: String = "PRODUCE",
    val bestBeforeDate: Date? = null,
    val daysLeft: Int = 0,
    val quantity: Int = 1,
    val imageUri: Uri? = null, // <-- 2. INI YANG SEMPAT HILANG BANG
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)