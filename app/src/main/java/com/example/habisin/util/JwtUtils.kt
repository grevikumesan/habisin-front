package com.example.habisin.util

import android.util.Base64
import org.json.JSONObject

/**
 * Decodes the claims (payload) of a JWT without verifying the signature — we only need it to
 * read display info (email / username) the login endpoint doesn't return. Verification stays on BE.
 */
data class JwtClaims(val username: String?, val email: String?)

fun decodeJwtClaims(token: String?): JwtClaims {
    if (token.isNullOrBlank()) return JwtClaims(null, null)
    return try {
        val parts = token.split(".")
        if (parts.size < 2) return JwtClaims(null, null)
        val payload = String(
            Base64.decode(parts[1], Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP)
        )
        val json = JSONObject(payload)

        // Cover the common claim names different BE setups use.
        val username = json.optStringOrNull("username")
            ?: json.optStringOrNull("name")
            ?: json.optStringOrNull("fullName")
        val email = json.optStringOrNull("email")

        JwtClaims(username, email)
    } catch (e: Exception) {
        JwtClaims(null, null)
    }
}

private fun JSONObject.optStringOrNull(key: String): String? {
    if (!has(key) || isNull(key)) return null
    val value = optString(key, "")
    return value.ifBlank { null }
}
