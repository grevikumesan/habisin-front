package com.example.habisin.data.remote.service

import com.example.habisin.data.remote.dto.SubscribeResponse
import com.example.habisin.data.remote.dto.SubscriptionStatusResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface PaymentService {

    @POST("payment/subscribe")
    suspend fun subscribe(): Response<SubscribeResponse>

    @GET("payment/status")
    suspend fun getStatus(): Response<SubscriptionStatusResponse>
}