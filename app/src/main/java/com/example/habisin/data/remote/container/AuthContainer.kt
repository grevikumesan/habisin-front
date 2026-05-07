package com.example.habisin.data.remote.container

import android.content.Context
import com.example.habisin.data.remote.repository.AuthRepository
import com.example.habisin.data.remote.service.AuthService
import com.example.habisin.util.SessionManager
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthContainer(context: Context) {

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://127.0.0.1:3000/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val authService: AuthService =
        retrofit.create(AuthService::class.java)

    val sessionManager: SessionManager = SessionManager(context)

    val authRepository: AuthRepository = AuthRepository(authService, sessionManager)
}