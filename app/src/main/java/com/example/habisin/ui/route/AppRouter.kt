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
import com.example.habisin.ui.component.HabisinBottomNav
import com.example.habisin.ui.view.DashboardScreen
import com.example.habisin.ui.view.LoginScreen
import com.example.habisin.ui.view.RegisterScreen
import com.example.habisin.ui.view.fridge.MyFridgeScreen
import com.example.habisin.ui.view.profile.ProfileScreen
import com.example.habisin.ui.view.recipe.RecipeScreen
import com.example.habisin.ui.view.scan.AddProductScreen
import com.example.habisin.ui.recipe.RecipeDetailScreen
import com.example.habisin.ui.view.profile.AboutScreen
import com.example.habisin.ui.view.profile.AppLanguageScreen
import com.example.habisin.ui.view.profile.AppThemeScreen
import com.example.habisin.ui.view.profile.FaqScreen
import com.example.habisin.ui.view.profile.NotificationScreen
import com.example.habisin.ui.view.scan.BarcodeScannerScreen
import com.example.habisin.ui.view.scan.*
import com.example.habisin.ui.viewmodel.AddProductViewModel
import com.example.habisin.ui.viewmodel.LoginViewModel
import com.example.habisin.ui.viewmodel.MyFridgeViewModel
import com.example.habisin.ui.viewmodel.RecipeViewModel

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

    const val BARCODE_SCANNER = "BarcodeScanner"


    fun recipeDetail(id: String) = "RecipeDetail/$id"
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
        Routes.LANGUAGE,
        Routes.THEME,
        Routes.NOTIFICATION,
        Routes.FAQ,
        Routes.ABOUT,
        Routes.BARCODE_SCANNER
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
                    val viewModel: MyFridgeViewModel = viewModel()

                    // ✅ Reload every time this destination is (re)entered
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    LaunchedEffect(navBackStackEntry) {
                        if (navBackStackEntry?.destination?.route == Routes.FRIDGE) {
                            viewModel.loadProducts()
                        }
                    }

                    MyFridgeScreen(
                        viewModel = viewModel,
                        onNavigateToAddProduct = { navController.navigate(Routes.ADD_PRODUCT) }
                    )
                }

                composable(Routes.RECIPE) {
                    // TODO (teammate): inject RecipeViewModel & ambil list dari sana
                    RecipeScreen(
                        recipes       = emptyList(),
                        onRecipeClick = { recipeId ->
                            navController.navigate(Routes.recipeDetail(recipeId))
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
                    val addProductViewModel: AddProductViewModel = viewModel()
                    AddProductScreen(
                        onNavigateBack = { navController.popBackStack() },
                        onNavigateToFridge = {
                            navController.popBackStack(Routes.FRIDGE, inclusive = false) // ← change this
                        },
                        onNavigateToScanner = {
                            navController.navigate(Routes.BARCODE_SCANNER)
                        },
                        viewModel = addProductViewModel
                    )
                }

                composable(Routes.BARCODE_SCANNER) {

                    val addProductViewModel: AddProductViewModel =
                        viewModel(navController.previousBackStackEntry!!)

                    CameraPermissionScreen(
                        onPermissionDenied = {
                            navController.popBackStack(Routes.ADD_PRODUCT, inclusive = false)
                        }
                    ) {

                        BarcodeScannerScreen(

                            onBarcodeScanned = { barcode ->

                                addProductViewModel.fetchProductByBarcode(barcode)

                                navController.navigate(Routes.ADD_PRODUCT) {
                                    popUpTo(Routes.BARCODE_SCANNER) { inclusive = true }
                                }
                            },

                            onBack = {
                                navController.popBackStack()
                            }
                        )
                    }
                }

                composable(Routes.RECIPE_DETAIL) {
                    val recipeViewModel: RecipeViewModel = viewModel()
                    // TODO (teammate): panggil recipeViewModel.loadDetail(recipeId) di sini
                    // val recipeId = it.arguments?.getString("recipeId")
                    RecipeDetailScreen(viewModel = recipeViewModel)
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