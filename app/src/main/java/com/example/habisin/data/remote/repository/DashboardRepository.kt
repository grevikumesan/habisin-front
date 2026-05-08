package com.example.habisin.data.remote.repository

import com.example.habisin.data.remote.service.DashboardService
import com.example.habisin.ui.model.DashboardModel
import com.example.habisin.util.SessionManager

class DashboardRepository(
    private val service: DashboardService,
    private val sessionManager: SessionManager
) {
    suspend fun getDashboard(): Result<DashboardModel> {
        return try {
            val token = sessionManager.getToken()
                ?: return Result.failure(IllegalStateException("Not logged in"))

            val response = service.getDashboard("Bearer $token")
            if (response.isSuccessful) {
                response.body()?.data?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}