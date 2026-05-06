package com.example.habisin.ui.model

// UserModel — currently unused in auth flow since the backend encodes user info
// inside the JWT token. Use this if you need to display user info in the UI
// by decoding the token or adding a /me endpoint later.
data class UserModel(
    val id: Int,
    val username: String,
    val email: String
)