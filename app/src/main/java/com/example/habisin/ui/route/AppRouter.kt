package com.example.habisin.ui.router

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.habisin.ui.view.component.HabisinBottomNav
import com.example.habisin.ui.view.dashboard.DashboardScreen
import com.example.habisin.ui.view.auth.LoginScreen
import com.example.habisin.ui.view.auth.RegisterScreen
import com.example.habisin.ui.view.fridge.MyFridgeScreen
import com.example.habisin.ui.view.profile.ProfileScreen
import com.example.habisin.ui.view.recipe.RecipeScreen
import com.example.habisin.ui.view.scan.AddProductScreen
import com.example.habisin.ui.view.recipe.RecipeDetailScreen
import com.example.habisin.ui.view.profile.AboutScreen
import com.example.habisin.ui.view.profile.AppLanguageScreen
import com.example.habisin.ui.view.profile.AppThemeScreen
import com.example.habisin.ui.view.profile.FaqScreen
import com.example.habisin.ui.view.profile.NotificationScreen
import com.example.habisin.ui.viewmodel.LoginViewModel
import com.example.habisin.ui.viewmodel.RecipeViewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.habisin.ui.view.subscription.SubscriptionView

// ── Routes ──
object Routes {
    const val LOGIN          = "Login"
    const val REGISTER       = "Register"
    const val HOME           = "Home"
    const val FRIDGE         = "Fridge"
    const val RECIPE         = "Recipe"
    const val PROFILE        = "Profile"
    const val ADD_PRODUCT    = "AddProduct"
    const val RECIPE_DETAIL  = "RecipeDetail/{recipeId}"
    const val LANGUAGE       = "Language"
    const val THEME          = "Theme"
    const val NOTIFICATION   = "Notification"
    const val FAQ            = "Faq"
    const val ABOUT          = "About"
    const val SUBSCRIPTION   = "Subscription"

    fun recipeDetail(id: Int) = "RecipeDetail/$id"
}

@Composable
fun AppRouter() {

    val loginViewModel: LoginViewModel = viewModel()
    val navController: NavHostController = rememberNavController()

    val isLoggedIn by loginViewModel.sessionManager.isLoggedInFlow.collectAsState(initial = false)
    val startDestination = if (isLoggedIn) Routes.HOME else Routes.LOGIN

    // Screens yang ga nampilin bottom nav
    val screensWithoutBottomBar = listOf(
        Routes.LOGIN,
        Routes.REGISTER,
        Routes.ADD_PRODUCT,
        Routes.SUBSCRIPTION,
        Routes.LANGUAGE,
        Routes.THEME,
        Routes.NOTIFICATION,
        Routes.FAQ,
        Routes.ABOUT
    )

    // Untuk RecipeDetail (route dengan argument), match by prefix
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    val showBottomBar = currentRoute != null &&
            currentRoute !in screensWithoutBottomBar &&
            !currentRoute.startsWith("RecipeDetail")

    Surface(
        modifier = Modifier.fillMaxSize(),
        color    = Color.White
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                if (showBottomBar) {
                    HabisinBottomNav(
                        currentRoute = currentRoute ?: Routes.HOME,
                        onNavigate   = { route ->
                            navController.navigate(route) {
                                launchSingleTop = true
                                popUpTo(Routes.HOME) { saveState = true }
                                restoreState = true
                            }
                        },
                        onPlusClick  = { navController.navigate(Routes.ADD_PRODUCT) }
                    )
                }
            }
        ) { innerPadding ->
            NavHost(
                navController    = navController,
                startDestination = startDestination,
                modifier         = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {

                // ── Auth ──
                composable(Routes.LOGIN) {
                    LoginScreen(
                        onLoginSuccess       = {
                            navController.navigate(Routes.HOME) {
                                popUpTo(Routes.LOGIN) { inclusive = true }
                            }
                        },
                        onNavigateToRegister = { navController.navigate(Routes.REGISTER) },
                        viewModel            = loginViewModel
                    )
                }

                composable(Routes.REGISTER) {
                    RegisterScreen(
                        onRegisterSuccess = {
                            navController.navigate(Routes.HOME) {
                                popUpTo(Routes.LOGIN) { inclusive = true }
                            }
                        },
                        onNavigateToLogin = { navController.popBackStack() }
                    )
                }

                // ── Bottom nav screens ──
                composable(Routes.HOME) {
                    DashboardScreen(
                        onPlusClick    = { navController.navigate(Routes.ADD_PRODUCT) },
                        onItemClick    = { /* TODO: navigate ke detail item */ },
                        onViewAllClick = { navController.navigate(Routes.FRIDGE) },
                        onProfileClick = { navController.navigate(Routes.PROFILE) }
                    )
                }

                composable(Routes.FRIDGE) {
                    MyFridgeScreen(
                        onNavigateToAddProduct = { navController.navigate(Routes.ADD_PRODUCT) }
                    )
                }

                composable(Routes.RECIPE) {
                    val recipeViewModel: RecipeViewModel = viewModel()
                    val backStackEntry = navController.currentBackStackEntry

                    // ⬇️ Subscribe ke Flow, otomatis re-fire setiap kali nilai berubah
                    LaunchedEffect(backStackEntry) {
                        backStackEntry?.savedStateHandle
                            ?.getStateFlow("subscription_updated", false)
                            ?.collect { updated ->
                                android.util.Log.d("RECIPE_ROUTE", "Flag value: $updated")
                                if (updated) {
                                    android.util.Log.d("RECIPE_ROUTE", "Refetching recipes...")
                                    recipeViewModel.loadRecipes()
                                    // Reset flag biar nggak loop
                                    backStackEntry.savedStateHandle["subscription_updated"] = false
                                }
                            }
                    }

                    RecipeScreen(
                        viewModel = recipeViewModel,
                        onRecipeClick = { recipeId ->
                            navController.navigate(Routes.recipeDetail(recipeId))
                        },
                        onNavigateToSubscription = {
                            navController.navigate(Routes.SUBSCRIPTION)
                        }
                    )
                }

                composable(Routes.SUBSCRIPTION) {
                    SubscriptionView(
                        onBack = {
                            android.util.Log.d("SUB_BACK", "Setting flag & popping back stack")
                            navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set("subscription_updated", true)
                            navController.popBackStack()
                        }
                    )
                }

                composable(Routes.PROFILE) {
                    ProfileScreen(
                        onLoggedOut              = {
                            navController.navigate(Routes.LOGIN) {
                                popUpTo(0) { inclusive = true }
                            }
                        },
                        onNavigateToLanguage     = { navController.navigate(Routes.LANGUAGE) },
                        onNavigateToTheme        = { navController.navigate(Routes.THEME) },
                        onNavigateToNotification = { navController.navigate(Routes.NOTIFICATION) },
                        onNavigateToFaq          = { navController.navigate(Routes.FAQ) },
                        onNavigateToAbout        = { navController.navigate(Routes.ABOUT) }
                    )
                }

                // ── Detail / modal screens (tanpa bottom nav) ──
                composable(Routes.ADD_PRODUCT) {
                    AddProductScreen(
                        onNavigateBack = { navController.popBackStack() },
                        onNavigateToFridge = {
                            navController.navigate(Routes.FRIDGE) {
                                popUpTo(Routes.ADD_PRODUCT) { inclusive = true }
                            }
                        }
                    )
                }

                composable(
                    route = Routes.RECIPE_DETAIL,
                    arguments = listOf(navArgument("recipeId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val recipeId = backStackEntry.arguments?.getInt("recipeId") ?: return@composable
                    val recipeViewModel: RecipeViewModel = viewModel()  // ← polos
                    RecipeDetailScreen(
                        recipeId = recipeId,
                        viewModel = recipeViewModel,
                        onBack = { navController.popBackStack() }
                    )
                }

                // ── Profile sub-pages ──
                composable(Routes.LANGUAGE)     { AppLanguageScreen(onBack = { navController.popBackStack() }) }
                composable(Routes.THEME)        { AppThemeScreen(onBack = { navController.popBackStack() }) }
                composable(Routes.NOTIFICATION) { NotificationScreen(onBack = { navController.popBackStack() }) }
                composable(Routes.FAQ)          { FaqScreen(onBack = { navController.popBackStack() }) }
                composable(Routes.ABOUT)        { AboutScreen(onBack = { navController.popBackStack() }) }
            }
        }
    }
}