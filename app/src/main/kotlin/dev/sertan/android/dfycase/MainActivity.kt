package dev.sertan.android.dfycase

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint
import dev.sertan.android.dfycase.notification.DfyNotificationManager
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

    /**
     * Launcher for requesting notification permissions.
     * This is used to request the POST_NOTIFICATIONS permission on Android 13 and above.
     */
    private val requestPermissionLauncher = registerForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        callback = { isGranted ->
            if (!isGranted) showNotificationInfoDialog()
        }
    )

    /**
     * Checks if the app has permission to post notifications.
     * If not, it requests the permission.
     * This is necessary for Android 13 and above where the permission is required to post notifications
     */
    private fun askNotificationPermission() {
        if (DfyNotificationManager.canPostNotification(this)) return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        askNotificationPermission()
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

    /**
     * Shows a dialog to inform the user that notification permission is required.
     * This function is called when the user denies the notification permission request.
     */
    private fun showNotificationInfoDialog() {
        // TODO: Bildirim izni gerektiğini kullanıcıya bildir.
    }
}