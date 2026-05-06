package com.example.habisin.ui.router

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.habisin.ui.view.LoginScreen
import com.example.habisin.ui.view.RegisterScreen
import com.example.habisin.ui.viewmodel.LoginViewModel

// ─────────────────────────────────────────────────────────────────────────────
// TODO (teammates): import your ViewModels and Views here, e.g.:
// import com.example.habisin.ui.view.HomeView
// import com.example.habisin.ui.viewmodel.HomeViewModel
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun AppRouter() {

    // ── ViewModels ─────────────────────────────────────────────────────────
    val loginViewModel: LoginViewModel = viewModel()

    // TODO (teammates): add your ViewModels here, e.g.:
    // val homeViewModel: HomeViewModel = viewModel()

    // ── Nav controller & session check ─────────────────────────────────────
    val navController: NavHostController = rememberNavController()
    val isLoggedIn by loginViewModel.sessionManager.isLoggedInFlow.collectAsState(initial = false)
    val startDestination = if (isLoggedIn) "Home" else "Login"

    // ── Screens that should NOT show the bottom navigation bar ────────────
    // TODO (teammates): add your detail/modal screens here so the bottom bar
    // stays hidden on them, e.g. "ItemDetail/{itemId}", "EditProfile"
    val screensWithoutBottomBar = listOf("Login", "Register")

    Surface(modifier = Modifier.fillMaxSize()) {
        var selectedItemIndex by rememberSaveable { mutableStateOf(0) }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                val currentRoute = navController.currentBackStackEntryFlow
                    .collectAsState(null).value?.destination?.route

                if (currentRoute !in screensWithoutBottomBar) {
                    // TODO (teammates): build your bottom navigation bar here.
                    // Follow the same NavigationBar pattern from the previous project.
                    // Example structure:
                    //
                    // val bottomNavigationItems = listOf(
                    //     BottomNavigationItem(title = "Home",    icon = R.drawable.home_navbar),
                    //     BottomNavigationItem(title = "Profile", icon = R.drawable.profile_navbar),
                    // )
                    // NavigationBar(containerColor = Color(...)) {
                    //     bottomNavigationItems.forEachIndexed { index, item ->
                    //         NavigationBarItem(
                    //             selected = selectedItemIndex == index,
                    //             onClick = {
                    //                 selectedItemIndex = index
                    //                 navController.navigate(item.title) {
                    //                     launchSingleTop = true
                    //                     restoreState = true
                    //                 }
                    //             },
                    //             icon = { ... },
                    //             label = { ... }
                    //         )
                    //     }
                    // }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController    = navController,
                startDestination = startDestination,
                modifier         = Modifier.padding(innerPadding)
            ) {

                // ── Auth screens (handled by [your name]) ──────────────────
                composable("Login") {
                    LoginScreen(
                        onLoginSuccess       = {
                            navController.navigate("Home") {
                                popUpTo("Login") { inclusive = true }
                            }
                        },
                        onNavigateToRegister = { navController.navigate("Register") },
                        viewModel            = loginViewModel
                    )
                }

                composable("Register") {
                    RegisterScreen(
                        onRegisterSuccess = {
                            navController.navigate("Home") {
                                popUpTo("Login") { inclusive = true }
                            }
                        },
                        onNavigateToLogin = { navController.popBackStack() }
                    )
                }

                // ── TODO (teammates): add your screens below this line ─────
                //
                // Simple screen:
                // composable("Home") {
                //     HomeView(viewModel = homeViewModel, navController = navController)
                // }
                //
                // Screen with arguments:
                // composable("ItemDetail/{itemId}") { backStackEntry ->
                //     val id = backStackEntry.arguments?.getString("itemId")?.toIntOrNull() ?: 0
                //     ItemDetailView(itemId = id, navController = navController)
                // }
                // ─────────────────────────────────────────────────────────────
            }
        }
    }
}