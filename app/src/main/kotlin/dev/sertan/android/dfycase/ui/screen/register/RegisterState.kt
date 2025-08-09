package dev.sertan.android.dfycase.ui.screen.register

internal data class RegisterState(
    val email: String = "",
    val pass: String = "",
    val confirmPass: String = "",
    val isLoading: Boolean = false,
    val isRegisterSuccessful: Boolean = false,
    val isEmailValid: Boolean = true,
    val isPasswordValid: Boolean = true,
    val isPasswordsMatch: Boolean = true
)