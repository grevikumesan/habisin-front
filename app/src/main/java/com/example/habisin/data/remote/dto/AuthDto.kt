package com.example.habisin.data.remote.dto

//Request
data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    // TODO (backend team): confirm the exact field names your Express endpoint expects.
    // Add or remove fields here to match your POST /auth/register body.
    val username: String,
    val email: String,
    val password: String
)

//Response
data class AuthResponse(
    val data: TokenDto
)

data class TokenDto(
    val token: String
)

data class ApiErrorResponse(
    // TODO (backend team): confirm your Express error response shape.
    // e.g. { "message": "Invalid credentials" }
    val message: String
)