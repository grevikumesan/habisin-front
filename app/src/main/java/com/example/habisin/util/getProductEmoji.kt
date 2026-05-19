package com.example.habisin.util

fun getProductEmoji(name: String): String {
    return when {
        name.contains("Telur",     ignoreCase = true) -> "🥚"
        name.contains("Roti",   ignoreCase = true) -> "🥖"
        name.contains("Apel",   ignoreCase = true) -> "🍎"
        name.contains("Susu",    ignoreCase = true) -> "🥛"
        name.contains("Wortel",  ignoreCase = true) -> "🥕"
        name.contains("Bayam", ignoreCase = true) -> "🥬"
        name.contains("Ayam", ignoreCase = true) -> "🍗"
        name.contains("Ikan",    ignoreCase = true) -> "🐟"
        name.contains("Keju",  ignoreCase = true) -> "🧀"
        name.contains("Yogurt",  ignoreCase = true) -> "🥣"
        name.contains("Pisang",  ignoreCase = true) -> "🍌"
        name.contains("Tomat",  ignoreCase = true) -> "🍅"
        else -> "📦"
    }
}