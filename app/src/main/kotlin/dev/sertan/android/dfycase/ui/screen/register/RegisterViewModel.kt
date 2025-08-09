package dev.sertan.android.dfycase.ui.screen.register

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sertan.android.dfycase.domain.repository.UserRepository
import dev.sertan.android.dfycase.util.State
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterState())
    val uiState = _uiState.asStateFlow()

    fun onEmailInputChanged(email: String) {
        viewModelScope.launch {
            val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
            _uiState.update {
                it.copy(
                    email = email,
                    isEmailValid = isEmailValid
                )
            }
        }
    }

    fun onPasswordInputChanged(pwd: String) {
        viewModelScope.launch {
            val isPasswordValid = pwd.length >= 8
            _uiState.update {
                it.copy(
                    pass = pwd,
                    isPasswordValid = isPasswordValid
                )
            }
        }
    }

    fun onPasswordConfirmationInputChanged(pwd: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    confirmPass = pwd,
                    isPasswordsMatch = pwd == it.pass
                )
            }
        }
    }

    fun register() {
        val email = _uiState.value.email
        val pass = _uiState.value.pass
        val confirmPass = _uiState.value.confirmPass

        val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val isPasswordValid = pass.length >= 8
        val isPasswordMatch = pass == confirmPass

        _uiState.update {
            it.copy(
                isEmailValid = isEmailValid,
                isPasswordValid = isPasswordValid,
                isPasswordsMatch = isPasswordMatch
            )
        }

        if (!isEmailValid || !isPasswordValid || !isPasswordMatch) return

        userRepository.register(email, pass).onEach { result ->
            _uiState.update {
                when (result) {
                    is State.Error -> it.copy(isLoading = false, isRegisterSuccessful = false)
                    State.Loading -> it.copy(isLoading = true)
                    is State.Success -> it.copy(isLoading = false, isRegisterSuccessful = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}