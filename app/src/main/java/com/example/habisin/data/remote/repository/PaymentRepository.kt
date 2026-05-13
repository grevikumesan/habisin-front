package com.example.habisin.data.remote.repository

import com.example.habisin.data.remote.dto.SubscribeResponse
import com.example.habisin.data.remote.dto.SubscriptionStatusResponse
import com.example.habisin.data.remote.service.PaymentService
import retrofit2.Response

class PaymentRepository(
    private val service: PaymentService
) {
    suspend fun subscribe(): Response<SubscribeResponse> =
        service.subscribe()

    suspend fun getStatus(): Response<SubscriptionStatusResponse> =
        service.getStatus()
}