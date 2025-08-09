package dev.sertan.android.dfycase.ui.screen.login

/**
 * Represents the state of the Login screen.
 *
 * @property email The email input by the user.
 * @property pass The password input by the user.
 * @property isLoading Indicates if the login process is currently loading.
 * @property isLoginSuccessful Indicates if the login was successful.
 * @property isEmailValid Indicates if the entered email is valid.
 * @property isPasswordValid Indicates if the entered password is valid.
 * @property isValidationActive Indicates if validation checks are active.
 * @constructor Creates an instance of LoginState with default values.
 */
internal data class LoginState(
    val email: String = "",
    val pass: String = "",
    val isLoading: Boolean = false,
    val isLoginSuccessful: Boolean = false,
    val isEmailValid: Boolean = false,
    val isPasswordValid: Boolean = false,
    val isValidationActive: Boolean = false
)