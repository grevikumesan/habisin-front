package com.example.habisin.util

fun getProductEmoji(name: String): String {
    return when {
        name.contains("Egg",     ignoreCase = true) -> "🥚"
        name.contains("Bread",   ignoreCase = true) -> "🥖"
        name.contains("Apple",   ignoreCase = true) -> "🍎"
        name.contains("Milk",    ignoreCase = true) -> "🥛"
        name.contains("Carrot",  ignoreCase = true) -> "🥕"
        name.contains("Spinach", ignoreCase = true) -> "🥬"
        name.contains("Chicken", ignoreCase = true) -> "🍗"
        name.contains("Fish",    ignoreCase = true) -> "🐟"
        name.contains("Cheese",  ignoreCase = true) -> "🧀"
        name.contains("Yogurt",  ignoreCase = true) -> "🥣"
        name.contains("Banana",  ignoreCase = true) -> "🍌"
        name.contains("Tomato",  ignoreCase = true) -> "🍅"
        else -> "📦"
    }
}