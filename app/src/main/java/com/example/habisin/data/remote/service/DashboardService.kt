package com.example.habisin.data.remote.service

import com.example.habisin.data.remote.dto.DashboardResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface DashboardService {
    @GET("dashboard/")
    suspend fun getDashboard(
        @Header("Authorization") token: String
    ): Response<DashboardResponse>
}