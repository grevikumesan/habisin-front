package com.example.habisin.ui.router

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.habisin.ui.view.component.HabisinBottomNav
import com.example.habisin.ui.view.dashboard.DashboardScreen
import com.example.habisin.ui.view.auth.LoginScreen
import com.example.habisin.ui.view.auth.RegisterScreen
import com.example.habisin.ui.view.fridge.MyFridgeScreen
import com.example.habisin.ui.view.profile.ProfileScreen
import com.example.habisin.ui.view.recipe.RecipeScreen
import com.example.habisin.ui.view.recipe.RecipeDetailScreen
import com.example.habisin.ui.view.scan.AddProductScreen
import com.example.habisin.ui.view.scan.BarcodeScannerScreen
import com.example.habisin.ui.view.scan.CameraPermissionScreen
import com.example.habisin.ui.view.profile.AboutScreen
import com.example.habisin.ui.view.profile.AppLanguageScreen
import com.example.habisin.ui.view.profile.AppThemeScreen
import com.example.habisin.ui.view.profile.FaqScreen
import com.example.habisin.ui.view.profile.NotificationScreen
import com.example.habisin.ui.view.subscription.SubscriptionView
import com.example.habisin.ui.viewmodel.AddProductViewModel
import com.example.habisin.ui.viewmodel.DashboardViewModel
import com.example.habisin.ui.viewmodel.LoginViewModel
import com.example.habisin.ui.viewmodel.MyFridgeViewModel
import com.example.habisin.ui.viewmodel.RecipeViewModel

// ── Routes ──
object Routes {
    const val LOGIN           = "Login"
    const val REGISTER        = "Register"
    const val HOME            = "Home"
    const val FRIDGE          = "Fridge"
    const val RECIPE          = "Recipe"
    const val PROFILE         = "Profile"
    const val ADD_PRODUCT     = "AddProduct"
    const val RECIPE_DETAIL   = "RecipeDetail/{recipeId}?catalog={catalog}"
    const val LANGUAGE        = "Language"
    const val THEME           = "Theme"
    const val NOTIFICATION    = "Notification"
    const val FAQ             = "Faq"
    const val ABOUT           = "About"
    const val SUBSCRIPTION    = "Subscription"
    const val BARCODE_SCANNER = "BarcodeScanner"

    // catalog=true → browse catalog detail (GET /catalog/:id); false → saved/generated (GET /resep/:id)
    fun recipeDetail(id: Int, catalog: Boolean = true) = "RecipeDetail/$id?catalog=$catalog"
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
        Routes.ABOUT,
        Routes.BARCODE_SCANNER
    )

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    val showBottomBar = currentRoute != null &&
            currentRoute !in screensWithoutBottomBar &&
            !currentRoute.startsWith("RecipeDetail")

    // Kalau session ke-clear pas lagi login (logout / token 401 expired), balik ke Login.
    // Pakai guard wasLoggedIn biar nggak salah-trigger pas cold start (false → true).
    var wasLoggedIn by remember { mutableStateOf(false) }
    LaunchedEffect(isLoggedIn) {
        if (wasLoggedIn && !isLoggedIn &&
            currentRoute != Routes.LOGIN && currentRoute != Routes.REGISTER
        ) {
            navController.navigate(Routes.LOGIN) {
                popUpTo(0) { inclusive = true }
            }
        }
        wasLoggedIn = isLoggedIn
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color    = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            modifier  = Modifier.fillMaxSize(),
            // transparent so the themed Surface above shows through (Scaffold defaults to a light surface)
            containerColor = Color.Transparent,
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
                    val dashboardViewModel: DashboardViewModel = viewModel()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()

                    LaunchedEffect(navBackStackEntry) {
                        if (navBackStackEntry?.destination?.route == Routes.HOME) {
                            dashboardViewModel.loadDashboard()
                        }
                    }

                    DashboardScreen(
                        onPlusClick    = { navController.navigate(Routes.ADD_PRODUCT) },
                        onItemClick    = { navController.navigate(Routes.FRIDGE) },
                        onViewAllClick = { navController.navigate(Routes.FRIDGE) },
                        onProfileClick = { navController.navigate(Routes.PROFILE) }
                    )
                }

                composable(Routes.FRIDGE) {
                    val fridgeViewModel: MyFridgeViewModel = viewModel()

                    // Reload setiap kali destinasi ini di-enter ulang
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    LaunchedEffect(navBackStackEntry) {
                        if (navBackStackEntry?.destination?.route == Routes.FRIDGE) {
                            fridgeViewModel.loadProducts()
                        }
                    }

                    MyFridgeScreen(
                        viewModel = fridgeViewModel,
                        onNavigateToAddProduct = { navController.navigate(Routes.ADD_PRODUCT) }
                    )
                }

                composable(Routes.RECIPE) {
                    val recipeViewModel: RecipeViewModel = viewModel()
                    val backStackEntry = navController.currentBackStackEntry
                    val generatedRecipeId by recipeViewModel.generatedRecipeId.collectAsState()  // ← add

                    // Navigate to detail when generate succeeds
                    LaunchedEffect(generatedRecipeId) {                                           // ← add
                        generatedRecipeId?.let { id ->
                            navController.navigate(Routes.recipeDetail(id, catalog = false))
                            recipeViewModel.clearGeneratedRecipeId()
                        }
                    }

                    LaunchedEffect(backStackEntry) {
                        backStackEntry?.savedStateHandle
                            ?.getStateFlow("subscription_updated", false)
                            ?.collect { updated ->
                                if (updated) {
                                    recipeViewModel.loadRecipes()
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
                        },
                        onGenerateClick = {
                            recipeViewModel.generateResep(saveToHistory = true)
                        }
                    )
                }

                composable(Routes.SUBSCRIPTION) {
                    SubscriptionView(
                        onBack = {
                            Log.d("SUB_BACK", "Setting flag & popping back stack")
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
                        onNavigateToAbout        = { navController.navigate(Routes.ABOUT) },
                        onNavigateToSubscription = { navController.navigate(Routes.SUBSCRIPTION) }
                    )
                }

                // ── Detail / modal screens (tanpa bottom nav) ──
                composable(Routes.ADD_PRODUCT) {
                    val addProductViewModel: AddProductViewModel = viewModel()
                    AddProductScreen(
                        onNavigateBack = { navController.popBackStack() },
                        onNavigateToFridge = {
                            navController.navigate(Routes.FRIDGE) {
                                popUpTo(Routes.HOME) {
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }
                        },
                        onNavigateToScanner = {
                            navController.navigate(Routes.BARCODE_SCANNER)
                        },
                        viewModel = addProductViewModel
                    )
                }

                composable(Routes.BARCODE_SCANNER) {
                    // Share VM dengan AddProduct (previous entry)
                    val addProductViewModel: AddProductViewModel =
                        viewModel(navController.previousBackStackEntry!!)

                    // Guard biar callback gak double-fire (scan + back race)
                    var hasHandledScan by remember { mutableStateOf(false) }

                    CameraPermissionScreen(
                        onPermissionDenied = {
                            navController.popBackStack(Routes.ADD_PRODUCT, inclusive = false)
                        }
                    ) {
                        BarcodeScannerScreen(
                            onBarcodeScanned = { barcode ->
                                if (!hasHandledScan) {
                                    hasHandledScan = true
                                    Log.d("BarcodeScan", "Captured barcode: $barcode")
                                    addProductViewModel.fetchProductByBarcode(barcode)
                                    navController.popBackStack()
                                }
                            },
                            onBack = {
                                if (!hasHandledScan) {
                                    hasHandledScan = true
                                    navController.popBackStack()
                                }
                            }
                        )
                    }
                }

                composable(
                    route = Routes.RECIPE_DETAIL,
                    arguments = listOf(
                        navArgument("recipeId") { type = NavType.IntType },
                        navArgument("catalog") { type = NavType.BoolType; defaultValue = true }
                    )
                ) { backStackEntry ->
                    val recipeId = backStackEntry.arguments?.getInt("recipeId") ?: return@composable
                    val isCatalog = backStackEntry.arguments?.getBoolean("catalog") ?: true
                    val recipeViewModel: RecipeViewModel = viewModel()
                    RecipeDetailScreen(
                        recipeId = recipeId,
                        isCatalog = isCatalog,
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