package com.example.habisin.util

/**
 * No real photos in the catalog yet (BE returns imageUrl = null), so we fetch a food photo from
 * the internet keyed off the recipe name. Indonesian dish names aren't good image-search tags
 * (they return junk — e.g. "tumis kangkung" → a random cat), so we map common Indonesian food
 * words to reliable English tags. `lock` makes the SAME recipe always resolve to the SAME photo.
 *
 * Needs the device to have internet. If the BE ever provides a real imageUrl, that takes
 * precedence — this is only the fallback.
 */
fun recipeStockImageUrl(name: String, seed: Int): String {
    val n = name.lowercase()
    val tag = when {
        listOf("mie", "bakmi", "gacoan", "pasta", "spaghetti").any { n.contains(it) } -> "noodles"
        listOf("nasi", "rice").any { n.contains(it) } -> "rice"
        listOf("ayam", "chicken").any { n.contains(it) } -> "fried,chicken"
        listOf("ikan", "fish", "seafood", "udang", "cumi").any { n.contains(it) } -> "fish"
        listOf("rendang", "daging", "beef", "sapi", "steak").any { n.contains(it) } -> "beef"
        listOf("tahu", "tofu").any { n.contains(it) } -> "tofu"
        listOf("tempe", "tempeh").any { n.contains(it) } -> "tempeh"
        listOf("telur", "egg").any { n.contains(it) } -> "egg,dish"
        listOf("soto", "sop", "soup", "bakso", "rawon").any { n.contains(it) } -> "soup"
        listOf("sayur", "tumis", "kangkung", "kacang", "capcay", "urap", "salad", "buncis")
            .any { n.contains(it) } -> "vegetable"
        listOf("sambal", "cabai", "chili", "bumbu").any { n.contains(it) } -> "chili,sauce"
        listOf("kue", "putu", "klepon", "jajan", "dessert", "puding", "manis").any { n.contains(it) } -> "dessert"
        listOf("es ", "jus", "teh", "kopi", "minuman", "drink").any { n.contains(it) } -> "drink"
        else -> "indonesian"
    }
    return "https://loremflickr.com/600/400/$tag,food?lock=$seed"
}
