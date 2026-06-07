package com.example.habisin.data.remote

import android.os.Build

/**
 * Picks the backend address automatically:
 *   • Emulator    → 10.0.2.2 (the emulator's alias for the host machine)
 *   • Real phone  → LAN_HOST (the laptop's Wi-Fi IP), reached over the same WiFi
 *
 * So ONE APK works on both — no manual switching. For a phone demo, set LAN_HOST to
 * the laptop's IPv4 on the demo network (`ipconfig` → Wi-Fi adapter), then rebuild.
 * The phone needs no adb/USB — just the same WiFi as the laptop running the backend.
 */
object ApiConfig {
    // On campus WiFi (client isolation) the phone can't reach the laptop's LAN IP, so we
    // tunnel over USB instead: run `adb reverse tcp:3000 tcp:3000`, and the phone reaches
    // the laptop's backend via 127.0.0.1. (For a normal home WiFi / hotspot with no isolation,
    // set this back to the laptop's Wi-Fi IPv4, e.g. "10.0.89.96".)
    private const val LAN_HOST = "127.0.0.1"

    private val isEmulator: Boolean
        get() = Build.FINGERPRINT.startsWith("generic") ||
                Build.FINGERPRINT.lowercase().contains("emulator") ||
                Build.MODEL.contains("google_sdk") ||
                Build.MODEL.contains("Emulator") ||
                Build.MODEL.contains("Android SDK built for") ||
                Build.PRODUCT.contains("sdk") ||
                Build.HARDWARE.contains("goldfish") ||
                Build.HARDWARE.contains("ranchu")

    private val host: String
        get() = if (isEmulator) "http://10.0.2.2:3000" else "http://$LAN_HOST:3000"

    val BASE_URL: String
        get() = "$host/api/"

    /**
     * Resolve an image path for Coil: full http(s) URLs pass through (e.g. Open Food Facts),
     * relative ones like "/uploads/abc.jpg" get the backend host prepended. Null/blank → null.
     */
    fun imageUrl(path: String?): String? = when {
        path.isNullOrBlank() -> null
        path.startsWith("http", ignoreCase = true) -> path
        else -> "$host/${path.trimStart('/')}"
    }
}
