package dev.sertan.android.dfycase.ui.screen.home

import dev.sertan.android.dfycase.data.model.File

internal data class HomeState(
    val files: List<File> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isTopMenuExpanded: Boolean = false,
    val isUserLoggedOut: Boolean = false
)
