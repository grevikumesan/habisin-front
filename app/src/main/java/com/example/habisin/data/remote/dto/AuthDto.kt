package com.example.habisin.data.remote.dto

//Request
data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(

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
    val message: String
)