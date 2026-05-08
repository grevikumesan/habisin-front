package com.example.habisin.data.remote.dto

import java.util.Date

data class AddFoodRequest(
    val foodName: String,
    val descriptionFood: String = "",
    val bestBefore: Date,
    val quantity: Int,
    val category: String          // "PRODUCE", "DIARY", "MEAT", "OTHER"
)