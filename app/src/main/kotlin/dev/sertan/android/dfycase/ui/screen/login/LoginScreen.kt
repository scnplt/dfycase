package dev.sertan.android.dfycase.ui.screen.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.sertan.android.dfycase.R
import dev.sertan.android.dfycase.ui.common.EmailTextField
import dev.sertan.android.dfycase.ui.common.PasswordTextField
import dev.sertan.android.dfycase.ui.theme.DFYCaseTheme
import kotlinx.serialization.Serializable

/**
 * Represents the Login route in the application.
 */
@Serializable
internal object LoginRoute


/**
 * Composable function that displays the Login screen.
 * This screen allows users to log in with their email and password,
 * and provides an option to navigate to the registration screen.
 *
 * @param onLoginSuccess A lambda function to be called when the login is successful.
 * @param onRegisterClicked A lambda function to navigate to the registration screen.
 */
@Composable
internal fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onRegisterClicked: () -> Unit,
) {
    val viewModel = hiltViewModel<LoginViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.isLoginSuccessful) onLoginSuccess()

    LoginScreen(
        uiState = uiState,
        onEmailChanged = viewModel::onEmailInputChanged,
        onPasswordChanged = viewModel::onPasswordInputChanged,
        onLoginClicked = viewModel::login,
        onRegisterClicked = onRegisterClicked
    )
}

@Composable
private fun LoginScreen(
    uiState: LoginState,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onLoginClicked: () -> Unit,
    onRegisterClicked: () -> Unit
) {
    Scaffold { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.app_name),
                    fontSize = 64.sp,
                    fontWeight = FontWeight.Bold,
                )

                Spacer(modifier = Modifier.height(32.dp))

                EmailTextField(
                    value = uiState.email,
                    onValueChange = onEmailChanged,
                    modifier = Modifier.fillMaxWidth(),
                    isError = uiState.isValidationActive && !uiState.isEmailValid
                )

                Spacer(modifier = Modifier.height(16.dp))

                PasswordTextField(
                    value = uiState.pass,
                    onValueChange = onPasswordChanged,
                    imeAction = ImeAction.Done,
                    keyboardActions = KeyboardActions(onDone = { onLoginClicked() }),
                    modifier = Modifier.fillMaxWidth(),
                    isError = uiState.isValidationActive && !uiState.isPasswordValid,
                    label = stringResource(id = R.string.password)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onLoginClicked,
                    modifier = Modifier.fillMaxWidth(0.8f),
                    enabled = !uiState.isLoading
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.secondary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    } else {
                        Text(text = stringResource(R.string.login))
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

                OutlinedButton(
                    onClick = onRegisterClicked, modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Text(text = stringResource(R.string.register))
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun PreviewLoginScreen() {
    DFYCaseTheme {
        LoginScreen(
            uiState = LoginState(),
            onEmailChanged = {},
            onPasswordChanged = {},
            onLoginClicked = {},
            onRegisterClicked = {})
    }
}
