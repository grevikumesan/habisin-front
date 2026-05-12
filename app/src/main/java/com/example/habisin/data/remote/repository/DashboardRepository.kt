package com.example.habisin.data.remote.repository

import com.example.habisin.data.remote.service.DashboardService
import com.example.habisin.ui.model.DashboardModel
import com.example.habisin.util.SessionManager

class DashboardRepository(
    private val service: DashboardService,
    private val sessionManager: SessionManager
) {
    suspend fun getDashboard(): Result<DashboardModel> {
        try {
            val token = sessionManager.getToken()
                ?: return Result.failure(IllegalStateException("Not logged in"))

            val response = service.getDashboard("Bearer $token")
            return if (response.isSuccessful) {
                val data = response.body()?.data
                if (data != null) {
                    Result.success(data)
                } else {
                    Result.failure(Exception("Empty response body"))
                }
            } else {
                val errorBody = response.errorBody()?.string()
                android.util.Log.e("Dashboard", "HTTP ${response.code()}: $errorBody")
                Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}