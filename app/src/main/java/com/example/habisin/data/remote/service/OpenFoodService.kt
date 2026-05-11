package com.example.habisin.data.remote.service

import com.example.habisin.data.remote.dto.OpenFoodResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface OpenFoodService {
    @GET("api/v0/product/{barcode}.json")
    suspend fun getProductByBarcode(
        @Path("barcode") barcode: String
    ): Response<OpenFoodResponse>
}
