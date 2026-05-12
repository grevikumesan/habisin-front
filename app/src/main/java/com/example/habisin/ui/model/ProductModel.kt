package com.example.habisin.ui.model

import java.util.Date
import java.util.concurrent.TimeUnit

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
) {
    val computedDaysLeft: Int
        get() {
            if (bestBeforeDate == null) return daysLeft
            val diff = bestBeforeDate.time - System.currentTimeMillis()
            return if (diff > 0) {
                TimeUnit.MILLISECONDS.toDays(diff).toInt()
            } else {
                0
            }
        }
}