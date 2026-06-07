package com.example.habisin.util

/**
 * No real photos in the catalog yet (BE returns imageUrl = null), so we fetch a food photo
 * from the internet keyed off the recipe name. LoremFlickr returns a Flickr image matching the
 * tags; `lock` makes the SAME recipe always resolve to the SAME photo (stable, not random).
 *
 * Needs the device to have internet access (separate from the backend). If a real imageUrl is
 * ever provided by the BE, that takes precedence — this is only the fallback.
 */
fun recipeStockImageUrl(name: String, seed: Int): String {
    val keywords = name.lowercase()
        .replace(Regex("[^a-z0-9 ]"), " ")
        .split(Regex("\\s+"))
        .filter { it.length >= 3 }
        .take(2)
    val tags = (keywords + listOf("indonesian", "food")).joinToString(",")
    return "https://loremflickr.com/600/400/$tags?lock=$seed"
}
