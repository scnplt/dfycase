package dev.sertan.android.dfycase.ui.common

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import dev.sertan.android.dfycase.R

/**
 * A composable function that displays a text field for entering an email address.
 */
@Composable
internal fun EmailTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    imeAction: ImeAction = ImeAction.Next,
    keyboardActions: KeyboardActions? = null
) {
    val focusManager = LocalFocusManager.current

    TextField(
        value = value,
        onValueChange = onValueChange,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = stringResource(R.string.email_address)
            )
        },
        keyboardOptions = KeyboardOptions(
            imeAction = imeAction,
            keyboardType = KeyboardType.Email
        ),
        keyboardActions = keyboardActions ?: KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        ),
        label = { Text(stringResource(R.string.email_address)) },
        singleLine = true,
        isError = isError,
        modifier = modifier
    )
}