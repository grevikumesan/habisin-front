package com.example.habisin.data.remote

/**
 * Single place to point the app at the backend.
 *
 * ┌─ HOW TO SET FOR EACH SETUP ────────────────────────────────────────────────┐
 * │ Emulator (default):   "http://10.0.2.2:3000/api/"                            │
 * │ Phone on same WiFi:   "http://<LAPTOP-IPv4>:3000/api/"                       │
 * │       e.g.            "http://192.168.1.5:3000/api/"                         │
 * └─────────────────────────────────────────────────────────────────────────────┘
 *
 * Finding the laptop's IPv4:
 *   • Windows: run `ipconfig`  → "IPv4 Address" under your WiFi adapter
 *   • macOS:   run `ipconfig getifaddr en0`
 *
 * For the phone demo the backend must listen on 0.0.0.0 (not just 127.0.0.1),
 * and the phone + laptop must be on the same WiFi. Cleartext (http) to any LAN
 * IP is already allowed by res/xml/network_security_config.xml.
 */
object ApiConfig {
    const val BASE_URL = "http://10.0.2.2:3000/api/"
}
