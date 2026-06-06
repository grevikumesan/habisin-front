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
    private const val LAN_HOST = "10.0.89.96"   // ← laptop's Wi-Fi IP for real phones

    private val isEmulator: Boolean
        get() = Build.FINGERPRINT.startsWith("generic") ||
                Build.FINGERPRINT.lowercase().contains("emulator") ||
                Build.MODEL.contains("google_sdk") ||
                Build.MODEL.contains("Emulator") ||
                Build.MODEL.contains("Android SDK built for") ||
                Build.PRODUCT.contains("sdk") ||
                Build.HARDWARE.contains("goldfish") ||
                Build.HARDWARE.contains("ranchu")

    val BASE_URL: String
        get() = if (isEmulator) "http://10.0.2.2:3000/api/" else "http://$LAN_HOST:3000/api/"
}
