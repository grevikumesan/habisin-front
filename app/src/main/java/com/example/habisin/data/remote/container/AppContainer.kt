package com.example.habisin.data.remote.container

import android.content.Context
import com.example.habisin.data.remote.repository.AuthRepository
import com.example.habisin.data.remote.repository.DashboardRepository
import com.example.habisin.data.remote.repository.FoodRepository
import com.example.habisin.data.remote.repository.PaymentRepository
import com.example.habisin.data.remote.repository.RecipeRepository
import com.example.habisin.data.remote.service.AuthService
import com.example.habisin.data.remote.service.DashboardService
import com.example.habisin.data.remote.service.FoodService
import com.example.habisin.data.remote.service.PaymentService
import com.example.habisin.data.remote.service.RecipeService
import com.example.habisin.util.SessionManager
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppContainer(context: Context) {

    val sessionManager: SessionManager = SessionManager(context)

    // OkHttpClient dengan auth interceptor otomatis
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val token = runBlocking { sessionManager.getToken() } // ✅ wrap dengan runBlocking
            val request = if (!token.isNullOrEmpty()) {
                chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
            } else {
                chain.request()
            }
            chain.proceed(request)
        }
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://127.0.0.1:3000/api/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val authService: AuthService = retrofit.create(AuthService::class.java)
    private val dashboardService: DashboardService = retrofit.create(DashboardService::class.java)
    private val foodService: FoodService = retrofit.create(FoodService::class.java)
    private val recipeService: RecipeService = retrofit.create(RecipeService::class.java)
    private val paymentService: PaymentService = retrofit.create(PaymentService::class.java)

    val authRepository: AuthRepository = AuthRepository(authService, sessionManager)
    val dashboardRepository: DashboardRepository = DashboardRepository(dashboardService, sessionManager)
    val foodRepository: FoodRepository = FoodRepository(foodService, sessionManager)
    val recipeRepository: RecipeRepository = RecipeRepository(recipeService)
    val paymentRepository: PaymentRepository = PaymentRepository(paymentService)

}