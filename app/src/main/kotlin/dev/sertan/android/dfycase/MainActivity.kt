package dev.sertan.android.dfycase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint
import dev.sertan.android.dfycase.ui.screen.home.HomeRoute
import dev.sertan.android.dfycase.ui.screen.home.HomeScreen
import dev.sertan.android.dfycase.ui.screen.login.LoginRoute
import dev.sertan.android.dfycase.ui.screen.login.LoginScreen
import dev.sertan.android.dfycase.ui.screen.register.RegisterRoute
import dev.sertan.android.dfycase.ui.screen.register.RegisterScreen
import dev.sertan.android.dfycase.ui.theme.DFYCaseTheme
import dev.sertan.android.dfycase.util.navAndPopBackStack

/**
 * Main activity of the application.
 * Handles navigation and permission requests.
 * Uses Jetpack Compose for UI.
 * Uses Hilt for dependency injection.
 */
@AndroidEntryPoint
internal class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DFYCaseTheme {
                val navController = rememberNavController()
                val startDestination =
                    if (Firebase.auth.currentUser != null) HomeRoute else LoginRoute

                NavHost(navController = navController, startDestination = startDestination) {
                    composable<LoginRoute> {
                        LoginScreen(
                            onLoginSuccess = {
                                navController.navAndPopBackStack(LoginRoute, HomeRoute)
                            },
                            onRegisterClicked = { navController.navigate(RegisterRoute) }
                        )
                    }
                    composable<RegisterRoute> {
                        RegisterScreen(
                            onNavigateLoginClicked = {
                                navController.navAndPopBackStack(RegisterRoute, LoginRoute)
                            },
                            onRegisterSuccess = {
                                navController.navAndPopBackStack(RegisterRoute, HomeRoute)
                            }
                        )
                    }
                    composable<HomeRoute> {
                        HomeScreen {
                            navController.navAndPopBackStack(HomeRoute, LoginRoute)
                        }
                    }
                }
            }
        }
    }
}