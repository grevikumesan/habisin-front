package com.example.habisin.data.remote.repository

import com.example.habisin.data.remote.dto.AuthResponse
import com.example.habisin.data.remote.dto.LoginRequest
import com.example.habisin.data.remote.dto.RegisterRequest
import com.example.habisin.data.remote.service.AuthService
import com.example.habisin.util.SessionManager
import com.example.habisin.util.decodeJwtClaims
import retrofit2.Response

class AuthRepository(
    private val service: AuthService,
    private val sessionManager: SessionManager
) {

    suspend fun login(email: String, password: String): Response<AuthResponse> {
        val response = service.login(LoginRequest(email, password))
        if (response.isSuccessful) {
            response.body()?.data?.token?.let { token ->
                sessionManager.saveSession(token)
                // Login cuma balikin token → ambil identitas dari JWT, fallback email yg diketik.
                val claims = decodeJwtClaims(token)
                sessionManager.saveUserInfo(
                    username = claims.username,
                    email    = claims.email ?: email
                )
            }
        }
        return response
    }

    suspend fun register(username: String, email: String, password: String): Response<AuthResponse> {
        val response = service.register(RegisterRequest(username, email, password))
        if (response.isSuccessful) {
            response.body()?.data?.token?.let { token ->
                sessionManager.saveSession(token)
                // Register: kita udah punya username & email dari form.
                sessionManager.saveUserInfo(username = username, email = email)
            }
        }
        return response
    }

    suspend fun logout() {
        sessionManager.clearSession()
    }

    suspend fun isLoggedIn(): Boolean = sessionManager.isLoggedIn()
}