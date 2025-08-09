package dev.sertan.android.dfycase.ui.screen.login

internal data class LoginState(
    val email: String = "",
    val pass: String = "",
    val isLoading: Boolean = false,
    val isLoginSuccessful: Boolean = false,
    val isEmailValid: Boolean = false,
    val isPasswordValid: Boolean = false,
    val isValidationActive: Boolean = false
)