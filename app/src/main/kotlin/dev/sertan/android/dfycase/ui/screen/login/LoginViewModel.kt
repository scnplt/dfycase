package dev.sertan.android.dfycase.ui.screen.login

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
internal class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginState())
    val uiState = _uiState.asStateFlow()

    fun onEmailInputChanged(email: String) {
        viewModelScope.launch {
            val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
            _uiState.update {
                it.copy(
                    email = email,
                    isEmailValid = isEmailValid,
                    isValidationActive = true
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
                    isPasswordValid = isPasswordValid,
                    isValidationActive = true
                )
            }
        }
    }

    fun login() {
        if (!_uiState.value.isEmailValid || !_uiState.value.isPasswordValid) return

        userRepository.login(
            email = _uiState.value.email,
            pass = _uiState.value.pass
        ).onEach { result ->
            _uiState.update {
                when (result) {
                    is State.Error -> it.copy(isLoading = false)
                    State.Loading -> it.copy(isLoading = true)
                    is State.Success -> it.copy(isLoading = false, isLoginSuccessful = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}