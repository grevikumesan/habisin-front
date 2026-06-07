package com.example.habisin.data.remote.service

import com.example.habisin.data.remote.dto.NotificationFeedResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NotificationApiService {
    // Auth header is added by the shared OkHttp interceptor.
    @GET("notifications")
    suspend fun getNotifications(
        @Query("days") days: Int
    ): Response<NotificationFeedResponse>
}
