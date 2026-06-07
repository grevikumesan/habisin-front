package com.example.habisin.util

/**
 * Hand-picked, exact photos per catalog dish. Fill the URL for each (Google the dish →
 * "Copy image address" → paste a direct .jpg/.png/.webp link). Empty = fall back to the
 * keyword photo. Keyed by the exact catalog name from GET /api/catalog.
 */
val CURATED_RECIPE_IMAGES: Map<String, String> = mapOf(
    "Nasi Goreng Kampung"  to "",
    "Telur Balado"         to "",
    "Capcay Kuah"          to "",
    "Tumis Kangkung Terasi" to "",
    "Tempe Orek Kering"    to "",
    "Sayur Asem"           to "",
    "Pisang Goreng"        to "",
    "Bakwan Sayur"         to "",
    "Es Teh Manis"         to "",
    "Wedang Jahe"          to "",
    "Sambal Terasi"        to "",
    "Sambal Bawang"        to "",
    "Rendang Daging Sapi"  to "",
    "Soto Ayam Lamongan"   to "",
    "Ayam Goreng Lengkuas" to "",
    "Perkedel Kentang"     to "",
)

fun curatedRecipeImage(name: String?): String? =
    name?.let { CURATED_RECIPE_IMAGES[it.trim()] }?.takeIf { it.isNotBlank() }
