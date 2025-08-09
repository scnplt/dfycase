package dev.sertan.android.dfycase.ui.screen.home

import dev.sertan.android.dfycase.data.model.File

/**
 * Represents the state of the Home screen.
 *
 * @property files List of files to be displayed.
 * @property isLoading Indicates if the data is currently being loaded.
 * @property error Error message if any error occurs during data loading.
 * @property isTopMenuExpanded Indicates if the top menu is expanded.
 * @property isUserLoggedOut Indicates if the user has logged out.
 * @constructor Creates an instance of HomeState with default values.
 */
internal data class HomeState(
    val files: List<File> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isTopMenuExpanded: Boolean = false,
    val isUserLoggedOut: Boolean = false
)
