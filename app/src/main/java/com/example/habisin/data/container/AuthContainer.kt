package com.example.habisin.data.container

import android.content.Context
import com.example.habisin.data.repository.AuthRepository
import com.example.habisin.data.service.AuthService
import com.example.habisin.util.SessionManager
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * AuthContainer — manually wires up every dependency for the auth feature.
 * Mirrors the same Container pattern used in the rest of the project.
 *
 * TODO (teammates): if your own containers also build a Retrofit instance,
 * consider extracting it into a shared AppContainer/NetworkContainer so the
 * whole app reuses one Retrofit instance with the same base URL and config.
 */
class AuthContainer(context: Context) {

    private val retrofit: Retrofit = Retrofit.Builder()
        // TODO (backend team): replace with your actual Express server base URL.
        // Local emulator  → "http://10.0.2.2:3000/"
        // Real device (same Wi-Fi) → "http://YOUR_PC_LOCAL_IP:3000/"
        .baseUrl("http://10.0.2.2:3000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val authService: AuthService =
        retrofit.create(AuthService::class.java)

    val sessionManager: SessionManager = SessionManager(context)

    val authRepository: AuthRepository = AuthRepository(authService, sessionManager)
}