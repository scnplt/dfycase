package dev.sertan.android.dfycase.ui.screen.login

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.sertan.android.dfycase.R
import dev.sertan.android.dfycase.ui.common.EmailTextField
import dev.sertan.android.dfycase.ui.common.PasswordTextField
import dev.sertan.android.dfycase.ui.theme.DFYCaseTheme

@Composable
internal fun LoginScreen() {
    // TODO: LoginScreenContent entrypoint
}

@Composable
private fun LoginScreenContent() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }


    Surface {
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
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            PasswordTextField(
                value = password,
                onValueChange = { password = it },
                imeAction = ImeAction.Done,
                keyboardActions = KeyboardActions(
                    onDone = {
                        // TODO: Login işlemi başlat
                    }
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text(text = stringResource(R.string.login))
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            OutlinedButton(
                onClick = {},
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text(text = stringResource(R.string.register))
            }

        }
    }
}

@PreviewLightDark
@Composable
private fun PreviewLoginScreen() {
    DFYCaseTheme { LoginScreenContent() }
}
