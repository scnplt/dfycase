package dev.sertan.android.dfycase.util

import androidx.navigation.NavController

internal fun NavController.navAndPopBackStack(from: Any, to: Any) {
    navigate(to) { popUpTo(from) { inclusive = true } }
}
