package dev.sertan.android.dfycase.ui.screen.register

/**
 * Represents the state of the Register screen.
 *
 * @property email The email input by the user.
 * @property pass The password input by the user.
 * @property confirmPass The confirmation password input by the user.
 * @property isLoading Indicates if the registration process is currently loading.
 * @property isRegisterSuccessful Indicates if the registration was successful.
 * @property isEmailValid Indicates if the entered email is valid.
 * @property isPasswordValid Indicates if the entered password is valid.
 * @property isPasswordsMatch Indicates if the entered passwords match.
 * @constructor Creates an instance of RegisterState with default values.
 */
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