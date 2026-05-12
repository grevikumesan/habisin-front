package com.example.habisin.ui.model

import java.util.Date

data class ProductModel(
    val id: String,
    val name: String,
    val quantity: Int = 1,
    val unit: String = "PCS",
    val category: String = "Other",
    val bestBeforeDate: Date? = null,
    val daysLeft: Int = 0,
    val imageUrl: String = "",
    val barcode: String? = null,
    val dateAdded: Date = Date(),
    val notes: String = ""
)