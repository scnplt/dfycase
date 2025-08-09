package dev.sertan.android.dfycase.util

import androidx.navigation.NavController

/**
 * Navigate to a destination and pop the back stack up to the specified destination.
 * This is useful for navigating to a new screen and removing the previous screen from the back stack.
 *
 * @param from The destination to pop up to.
 * @param to The destination to navigate to.
 */
internal fun NavController.navAndPopBackStack(from: Any, to: Any) {
    navigate(to) { popUpTo(from) { inclusive = true } }
}
