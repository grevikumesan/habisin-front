package com.example.habisin.data.remote.service

import com.example.habisin.data.remote.dto.AuthResponse
import com.example.habisin.data.remote.dto.LoginRequest
import com.example.habisin.data.remote.dto.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<AuthResponse>

    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<AuthResponse>
}