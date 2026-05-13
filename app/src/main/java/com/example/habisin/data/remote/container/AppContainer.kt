package com.example.habisin.data.remote.container

import android.content.Context
import com.example.habisin.data.remote.repository.AuthRepository
import com.example.habisin.data.remote.repository.DashboardRepository
import com.example.habisin.data.remote.repository.FoodRepository
import com.example.habisin.data.remote.service.AuthService
import com.example.habisin.data.remote.service.DashboardService
import com.example.habisin.data.remote.service.FoodService
import com.example.habisin.util.SessionManager
import retrofit2.Retrofit
import com.example.habisin.data.remote.repository.OpenFoodRepository
import com.example.habisin.data.remote.service.OpenFoodService
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory

class AppContainer(context: Context) {
    private val gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        .create()
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://127.0.0.1:3000/api/")
        .client(okHttpClient)                              // ← add this
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    private val authService: AuthService =
        retrofit.create(AuthService::class.java)

    private val dashboardService: DashboardService =
        retrofit.create(DashboardService::class.java)

    private val foodService: FoodService =
        retrofit.create(FoodService::class.java)

    val sessionManager: SessionManager = SessionManager(context)

    val authRepository: AuthRepository =
        AuthRepository(authService, sessionManager)

    val dashboardRepository: DashboardRepository =
        DashboardRepository(dashboardService, sessionManager)

    val foodRepository: FoodRepository =
        FoodRepository(foodService, sessionManager)

    //Barcode scan
    private val openFoodRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://world.openfoodfacts.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val openFoodService: OpenFoodService = openFoodRetrofit.create(OpenFoodService::class.java)

    val openFoodRepository: OpenFoodRepository = OpenFoodRepository(openFoodService)
}