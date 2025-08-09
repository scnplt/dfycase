package dev.sertan.android.dfycase.ui.screen.register

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
import androidx.compose.material3.HorizontalDivider
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
 * Represents the Register route in the application.
 */
@Serializable
internal object RegisterRoute


/**
 * Composable function that displays the Register screen.
 * This screen allows users to register with their email and password,
 * and provides an option to navigate to the login screen.
 *
 * @param onNavigateLoginClicked A lambda function to navigate to the login screen.
 * @param onRegisterSuccess A lambda function to be called when the registration is successful.
 */
@Composable
internal fun RegisterScreen(
    onNavigateLoginClicked: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    val viewModel = hiltViewModel<RegisterViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.isRegisterSuccessful) onRegisterSuccess()

    RegisterScreenContent(
        uiState = uiState,
        onEmailChanged = viewModel::onEmailInputChanged,
        onPasswordChanged = viewModel::onPasswordInputChanged,
        onConfirmPasswordChanged = viewModel::onPasswordConfirmationInputChanged,
        onRegisterClicked = viewModel::register,
        onNavigateLoginClicked = onNavigateLoginClicked
    )
}

@Composable
private fun RegisterScreenContent(
    uiState: RegisterState,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onConfirmPasswordChanged: (String) -> Unit,
    onRegisterClicked: () -> Unit,
    onNavigateLoginClicked: () -> Unit
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
                    isError = !uiState.isEmailValid
                )

                Spacer(modifier = Modifier.height(16.dp))

                PasswordTextField(
                    value = uiState.pass,
                    onValueChange = onPasswordChanged,
                    imeAction = ImeAction.Next,
                    modifier = Modifier.fillMaxWidth(),
                    isError = !uiState.isPasswordValid,
                    label = stringResource(id = R.string.password)
                )

                Spacer(modifier = Modifier.height(16.dp))

                PasswordTextField(
                    value = uiState.confirmPass,
                    onValueChange = onConfirmPasswordChanged,
                    imeAction = ImeAction.Done,
                    keyboardActions = KeyboardActions(onDone = { onRegisterClicked() }),
                    modifier = Modifier.fillMaxWidth(),
                    isError = !uiState.isPasswordsMatch,
                    label = "Confirm Password"
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onRegisterClicked,
                    modifier = Modifier.fillMaxWidth(0.8f),
                    enabled = !uiState.isLoading
                ) {
                    Text(text = stringResource(R.string.register))
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

                OutlinedButton(
                    onClick = onNavigateLoginClicked,
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Text(text = stringResource(R.string.go_to_login))
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun PreviewRegisterScreen() {
    DFYCaseTheme {
        RegisterScreenContent(
            uiState = RegisterState(),
            onEmailChanged = {},
            onPasswordChanged = {},
            onConfirmPasswordChanged = {},
            onRegisterClicked = {},
            onNavigateLoginClicked = {}
        )
    }
}
