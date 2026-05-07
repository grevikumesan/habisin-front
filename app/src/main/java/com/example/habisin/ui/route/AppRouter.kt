package com.example.habisin.ui.router

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.habisin.ui.view.LoginScreen
import com.example.habisin.ui.view.RegisterScreen
import com.example.habisin.ui.view.profile.AboutScreen
import com.example.habisin.ui.view.profile.AppLanguageScreen
import com.example.habisin.ui.view.profile.AppThemeScreen
import com.example.habisin.ui.view.profile.FaqScreen
import com.example.habisin.ui.view.profile.NotificationScreen
import com.example.habisin.ui.view.profile.ProfileScreen
import com.example.habisin.ui.viewmodel.LoginViewModel

@Composable
fun AppRouter() {

    // ── ViewModels ─────────────────────────────────────────────────────────
    val loginViewModel: LoginViewModel = viewModel()

    // ── Nav controller & session check ─────────────────────────────────────
    val navController: NavHostController = rememberNavController()
    val isLoggedIn by loginViewModel.sessionManager.isLoggedInFlow.collectAsState(initial = false)
    val startDestination = if (isLoggedIn) "Home" else "Login"

    val screensWithoutBottomBar = listOf("Login", "Register", "Language", "Theme", "Notification", "Faq", "About")

    Surface(modifier = Modifier.fillMaxSize()) {
        var selectedItemIndex by rememberSaveable { mutableStateOf(0) }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                val currentRoute = navController.currentBackStackEntryFlow
                    .collectAsState(null).value?.destination?.route

                if (currentRoute !in screensWithoutBottomBar) {

                }
            }
        ) { innerPadding ->
            NavHost(
                navController    = navController,
                startDestination = startDestination,
                modifier         = Modifier.padding(innerPadding)
            ) {

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

                // ── TODO (teammates): replace this placeholder with the real Home screen ──
                composable("Home") {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("🏠 Home (placeholder)")
                        Spacer(Modifier.height(8.dp))
                        Text("You are logged in!")
                        Spacer(Modifier.height(16.dp))
                        Button(onClick = { navController.navigate("Profile") }) {
                            Text("Go to Profile")
                        }
                    }
                }


                composable("Profile") {
                    ProfileScreen(
                        onLoggedOut              = {
                            navController.navigate("Login") {
                                popUpTo(0) { inclusive = true }
                            }
                        },
                        onNavigateToLanguage     = { navController.navigate("Language") },
                        onNavigateToTheme        = { navController.navigate("Theme") },
                        onNavigateToNotification = { navController.navigate("Notification") },
                        onNavigateToFaq          = { navController.navigate("Faq") },
                        onNavigateToAbout        = { navController.navigate("About") }
                    )
                }

                composable("Language") {
                    AppLanguageScreen(
                        onBack = {
                            navController.popBackStack()
                        }
                    )
                }

                composable("Theme") {
                    AppThemeScreen(
                        onBack = {
                            navController.popBackStack()
                        }
                    )
                }

                composable("Notification") {
                    NotificationScreen(
                        onBack = {
                            navController.popBackStack()
                        }
                    )
                }

                composable("Faq") {
                    FaqScreen(
                        onBack = {
                            navController.popBackStack()
                        }
                    )
                }

                composable("About") {
                    AboutScreen(
                        onBack = {
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}